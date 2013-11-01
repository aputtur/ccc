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


imgBusinessHome_ro = new Image();
imgBusinessHome_ro.src = "/media/images/bt_bus_BusinessHome_roll.gif";
imgBusinessHome_norm = new Image();
imgBusinessHome_norm.src = "/media/images/bt_bus_BusinessHome.gif";
imgBusOurServices_ro = new Image();
imgBusOurServices_ro.src = "/media/images/bt_bus_OurServices_roll.gif";
imgBusOurServices_norm = new Image();
imgBusOurServices_norm.src = "/media/images/bt_bus_OurServices.gif";

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
