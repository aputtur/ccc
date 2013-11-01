//--Academic header Rollover function
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

imgAcademiaHome_ro = new Image();
imgAcademiaHome_ro.src = "/media/images/bt_aca_AcademiaHome_roll.gif";
imgAcademiaHome_norm = new Image();
imgAcademiaHome_norm.src = "/media/images/bt_aca_AcademiaHome.gif";
imgACAOurServices_ro = new Image();
imgACAOurServices_ro.src = "/media/images/bt_aca_OurServices_roll.gif";
imgACAOurServices_norm = new Image();
imgACAOurServices_norm.src = "/media/images/bt_aca_OurServices.gif";

//--Section Landing
imgFindTitle_body_1_ro = new Image();
imgFindTitle_body_1_ro.src = "/media/images/bt_FindTitle_CU_roll.gif";
imgFindTitle_body_1_norm = new Image();
imgFindTitle_body_1_norm.src = "/media/images/bt_FindTitle_CU.gif";
imgFindTitle_body_2_ro = new Image();
imgFindTitle_body_2_ro.src = "/media/images/bt_FindTitle_CU_roll.gif";
imgFindTitle_body_2_norm = new Image();
imgFindTitle_body_2_norm.src = "/media/images/bt_FindTitle_CU.gif";
}
