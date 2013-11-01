//--Generic Header Rollover function
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


//--Header rollovers
imgBusiness_ro = new Image();
imgBusiness_ro.src = "/media/images/bt_generic_business_roll.gif";
imgBusiness_norm = new Image();
imgBusiness_norm.src = "/media/images/bt_generic_business.gif";
imgAcademic_ro = new Image();
imgAcademic_ro.src = "/media/images/bt_generic_academic_roll.gif";
imgAcademic_norm = new Image();
imgAcademic_norm.src = "/media/images/bt_generic_academic.gif";
imgServiceProviders_ro = new Image();
imgServiceProviders_ro.src = "/media/images/bt_generic_serviceProviders_roll.gif";
imgServiceProviders_norm = new Image();
imgServiceProviders_norm.src = "/media/images/bt_generic_serviceProviders.gif";
imgPublishers_ro = new Image();
imgPublishers_ro.src = "/media/images/bt_generic_publishers_roll.gif";
imgPublishers_norm = new Image();
imgPublishers_norm.src = "/media/images/bt_generic_publishers.gif";
imgAuthors_ro = new Image();
imgAuthors_ro.src = "/media/images/bt_generic_authors_roll.gif";
imgAuthors_norm = new Image();
imgAuthors_norm.src = "/media/images/bt_generic_authors.gif";

}
