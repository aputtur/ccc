whenever sqlerror exit failure rollback;
set trimspool on
set timing on
set echo on
spool DeleteAnonymousUsers.log

-- Create tables containing only those rows we'll keep

-- Get some audit information

select count(*) from ccc_user;
select count(*) from ccc_user_request_client;

create table keep_these_clients
parallel
tablespace cc_data
as 
 select curq.user_request_client_id, curq.user_id, curq.au_id, curq.last_session_start, 
        curq.last_cart_id, curq.always_invoice, curq.is_anonymous,
        curq.sp_photocopy_academic, curq.sp_photocopy_general, curq.sp_coursepack, 
        curq.sp_digital, curq.sp_republish, curq.sp_business_photocopy, 
        curq.sp_business_digital_internal, curq.sp_academic_license, curq.version, 
        curq.cre_user, curq.cre_dtm, curq.upd_user, curq.upd_dtm, curq.skip_quickprice,
        curq.sp_rightslink_reprint, curq.sp_rightslink_digital,
        curq.sp_share_ill, curq.rl_session_id
 from ccc_user_request_client curq
 join ccc_user cu on curq.user_id = cu.user_id
 where cu.user_identifier is not null
 or (cu.user_identifier is null 
    and (NVL(curq.upd_dtm, curq.cre_dtm) >= (sysdate - 60))
    );

select count(*) from keep_these_clients;

create table keep_these_users
parallel
tablespace cc_data
as 
 select u.user_id, u.user_type, u.party_id, u.user_identifier, u.oid_pk, u.version, u.cre_user, 
        u.cre_dtm, u.upd_user, u.upd_dtm, u.enforce_pwd_chg
 from ccc_user u
 where u.user_identifier is not null;

insert into keep_these_users
 select u.user_id, u.user_type, u.party_id, u.user_identifier, u.oid_pk, u.version, u.cre_user, 
        u.cre_dtm, u.upd_user, u.upd_dtm, u.enforce_pwd_chg
 from ccc_user u
 where u.user_identifier is null
     and u.user_id in
     (select k.user_id from keep_these_clients k
      join ccc_user u on u.user_id = k.user_id
     );

select count(*) from keep_these_users;

-- Disable constraints that prevent us from truncating the existing tables
alter table ccc_user_request_client disable constraint fk_user_request_client_user;

-- Truncate the existing tables
truncate table ccc_user drop storage;

truncate table ccc_user_request_client drop storage;

-- Insert from the new tables
insert into ccc_user (
 user_id, user_type, party_id, user_identifier, oid_pk, version, cre_user, cre_dtm, upd_user,
 upd_dtm, enforce_pwd_chg 
)
select 
 user_id, user_type, party_id, user_identifier, oid_pk, version, cre_user, cre_dtm, upd_user,
 upd_dtm, enforce_pwd_chg
from keep_these_users;
commit;

insert into ccc_user_request_client (
  user_request_client_id, user_id, au_id, last_session_start, last_cart_id, always_invoice,
  is_anonymous, sp_photocopy_academic, sp_photocopy_general, sp_coursepack, sp_digital,
  sp_republish, sp_business_photocopy, sp_business_digital_internal, sp_academic_license,
  version, cre_user, cre_dtm, upd_user, upd_dtm, skip_quickprice, sp_rightslink_reprint,
  sp_rightslink_digital, sp_share_ill, rl_session_id)
select 
  user_request_client_id, user_id, au_id, last_session_start, last_cart_id, always_invoice,
  is_anonymous, sp_photocopy_academic, sp_photocopy_general, sp_coursepack, sp_digital,
  sp_republish, sp_business_photocopy, sp_business_digital_internal, sp_academic_license,
  version, cre_user, cre_dtm, upd_user, upd_dtm, skip_quickprice, sp_rightslink_reprint,
  sp_rightslink_digital, sp_share_ill, rl_session_id
from keep_these_clients;
commit;

-- Now re-enable the FK constraint
alter table ccc_user_request_client enable constraint fk_user_request_client_user;

-- Get auditing information

select count(*) from ccc_user;
select count(*) from ccc_user_request_client;

--==============
-- END OF SCRIPT
--==============
