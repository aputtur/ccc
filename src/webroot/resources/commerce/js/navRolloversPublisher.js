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


imgPublishersHome_ro = new Image();
imgPublishersHome_ro.src = "/media/images/bt_pub_PublishersHome_roll.gif";
imgPublishersHome_norm = new Image();
imgPublishersHome_norm.src = "/media/images/bt_pub_PublishersHome.gif";
imgPublishersOurServices_ro = new Image();
imgPublishersOurServices_ro.src = "/media/images/bt_pub_OurServices_roll.gif";
imgPublishersOurServices_norm = new Image();
imgPublishersOurServices_norm.src = "/media/images/bt_pub_OurServices.gif";

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
