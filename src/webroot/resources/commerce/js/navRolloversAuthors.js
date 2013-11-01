//--Rollover function
if (document.images) {
function changeImages(normal,rollover) {
  document.images[normal].src = eval(rollover+".src");
  }
}

if (document.images) {
imgFindTitle_ro = new Image();
imgFindTitle_ro.src = "/media/images/bt_FindTitle_roll.gif";
imgFindTitle_norm = new Image();
imgFindTitle_norm.src = "/media/images/bt_FindTitle.gif";


imgAuthorsHome_ro = new Image();
imgAuthorsHome_ro.src = "/media/images/bt_auth_AuthorsHome_roll.gif";
imgAuthorsHome_norm = new Image();
imgAuthorsHome_norm.src = "/media/images/bt_auth_AuthorsHome.gif";
imgAuthorsOurServices_ro = new Image();
imgAuthorsOurServices_ro.src = "/media/images/bt_auth_OurServices_roll.gif";
imgAuthorsOurServices_norm = new Image();
imgAuthorsOurServices_norm.src = "/media/images/bt_auth_OurServices.gif";
imgAuthorsBeyondBook_ro = new Image();
imgAuthorsBeyondBook_ro.src = "/media/images/bt_auth_BeyondBook_roll.gif";
imgAuthorsBeyondBook_norm = new Image();
imgAuthorsBeyondBook_norm.src = "/media/images/bt_auth_BeyondBook.gif";

imgFindTitle_RH_1_ro = new Image();
imgFindTitle_RH_1_ro.src = "/media/images/bt_FindTitle_RH_roll.gif";
imgFindTitle_RH_1_norm = new Image();
imgFindTitle_RH_1_norm.src = "/media/images/bt_FindTitle_RH.gif";
imgFindTitle_RH_2_ro = new Image();
imgFindTitle_RH_2_ro.src = "/media/images/bt_FindTitle_RH_roll.gif";
imgFindTitle_RH_2_norm = new Image();
imgFindTitle_RH_2_norm.src = "/media/images/bt_FindTitle_RH.gif";
imgFindTitle_RH_3_ro = new Image();
imgFindTitle_RH_3_ro.src = "/media/images/bt_VerifyTitle_RH_roll.gif";
imgFindTitle_RH_3_norm = new Image();
imgFindTitle_RH_3_norm.src = "/media/images/bt_VerifyTitle_RH.gif";
}
