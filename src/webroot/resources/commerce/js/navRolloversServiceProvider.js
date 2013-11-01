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


imgSPHome_ro = new Image();
imgSPHome_ro.src = "/media/images/bt_sp_servProvidersHome_roll.gif";
imgSPHome_norm = new Image();
imgSPHome_norm.src = "/media/images/bt_sp_ServProvidersHome.gif";
imgSPOurServices_ro = new Image();
imgSPOurServices_ro.src = "/media/images/bt_sp_OurServices_roll.gif";
imgSPOurServices_norm = new Image();
imgSPOurServices_norm.src = "/media/images/bt_sp_OurServices.gif";

//--Section Landing
imgFindTitle_body_1_ro = new Image();
imgFindTitle_body_1_ro.src = "/media/images/bt_FindTitle_CU_roll.gif";
imgFindTitle_body_1_norm = new Image();
imgFindTitle_body_1_norm.src = "/media/images/bt_FindTitle_CU.gif";
imgFindTitle_body_2_ro = new Image();
imgFindTitle_body_2_ro.src = "/media/images/bt_FindTitle_CU_roll.gif";
imgFindTitle_body_2_norm = new Image();
imgFindTitle_body_2_norm.src = "/media/images/bt_FindTitle_CU.gif";
imgFindTitle_RH_1_ro = new Image();
}
