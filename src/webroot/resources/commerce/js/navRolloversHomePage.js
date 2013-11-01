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

//--Home rollovers
imgHBusinessUse_ro = new Image();
imgHBusinessUse_ro.src = "/media/images/bt_home_businessUse_roll.gif";
imgHBusinessUse_norm = new Image();
imgHBusinessUse_norm.src = "/media/images/bt_home_businessUse.gif";
imgHAcademicUse_ro = new Image();
imgHAcademicUse_ro.src = "/media/images/bt_home_academicUse_roll.gif";
imgHAcademicUse_norm = new Image();
imgHAcademicUse_norm.src = "/media/images/bt_home_academicUse.gif";
imgHServProviders_ro = new Image();
imgHServProviders_ro.src = "/media/images/bt_home_serviceProviders_roll.gif";
imgHServProviders_norm = new Image();
imgHServProviders_norm.src = "/media/images/bt_home_serviceProviders.gif";
imgHAuthors_ro = new Image();
imgHAuthors_ro.src = "/media/images/bt_home_forAuthors_roll.gif";
imgHAuthors_norm = new Image();
imgHAuthors_norm.src = "/media/images/bt_home_forAuthors.gif";
imgHPublishers_ro = new Image();
imgHPublishers_ro.src = "/media/images/bt_home_forPublishers_roll.gif";
imgHPublishers_norm = new Image();
imgHPublishers_norm.src = "/media/images/bt_home_forPublishers.gif";

imgContentInfo_norm = new Image();
imgContentInfo_norm.src = "/media/images/home_CUText.gif";
imgContentInfo_bus_ro = new Image();
imgContentInfo_bus_ro.src = "/media/images/home_CUText_bus.gif";
imgContentInfo_acad_ro = new Image();
imgContentInfo_acad_ro.src = "/media/images/home_CUText_acad.gif";
imgContentInfo_serv_ro = new Image();
imgContentInfo_serv_ro.src = "/media/images/home_CUText_serv.gif";
imgRightsInfo_norm = new Image();
imgRightsInfo_norm.src = "/media/images/home_RHText.gif";
imgRightsInfo_pub_ro = new Image();
imgRightsInfo_pub_ro.src = "/media/images/home_RHText_pub.gif";
imgRightsInfo_auth_ro = new Image();
imgRightsInfo_auth_ro.src = "/media/images/home_RHText_auth.gif";
}
