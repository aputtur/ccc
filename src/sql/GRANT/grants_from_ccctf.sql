/*
 * grants_from_ccctf
 *
 * This script defines the privileges that ccctf grants on it's
 * objects to the CC user
 *
 * This script should be relocated into the CCCTF schema directory
 * and renamed 'grants_to_others.sql' when we've settled some decisions around
 * revision control on db objects
 */ 

grant select,update on ccctf.ccc_order to cc;
