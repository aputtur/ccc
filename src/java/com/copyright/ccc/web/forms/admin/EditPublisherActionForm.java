package com.copyright.ccc.web.forms.admin;

import java.util.ArrayList;
import java.util.List;

import com.copyright.ccc.business.data.RLinkPublisher;
import com.copyright.ccc.web.CCValidatorForm;
import com.copyright.ccc.web.util.PublisherDisplay;
import com.copyright.ccc.web.util.WebUtils;

public class EditPublisherActionForm extends CCValidatorForm {
	private static final long serialVersionUID = 1L;
	
    private RLinkPublisher rlinkPublisher;
    private boolean editMode=false;
    private boolean saveAllowed=true;
    private String accountNum;
    private String pubName;
    private String pubUrl;
    private String permOptionDesc;
    private String learnMoreDesc;
    private int selectedSubAccountRow;
    private RLinkPublisher addedSubAccount;
    private String addedSubAccountNum="";
    private String lastSaved="";
    private boolean subAccountSaveAllowed=false;
    
    private List<PublisherDisplay> subAccounts = new ArrayList<PublisherDisplay>();
    
    public EditPublisherActionForm() {
    }
    
    public RLinkPublisher getPublisher(){
        return rlinkPublisher;
    }
    
    public void setPublisher(RLinkPublisher rlinkPublisher){
        this.rlinkPublisher = rlinkPublisher;
    }
    
    public Boolean getEditMode(){
        return editMode;
    }
    
    public void setEditMode(boolean editMode){
        this.editMode = editMode;
    }
    
    /*
     * set the subaccounts for the account
     */
    
    public void setSubAccounts(RLinkPublisher[] rlSubAccountPublishers) {
        //translate collection publishers into collection with boolean for noting which rows selected
        subAccounts = new ArrayList<PublisherDisplay>();
        for (RLinkPublisher rlSubAccountPublisher: rlSubAccountPublishers) { 
            PublisherDisplay publisherDisplay = new PublisherDisplay(rlSubAccountPublisher,false);
            subAccounts.add(publisherDisplay);
        }
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
        if ( rlinkPublisher != null && WebUtils.isAllDigit(accountNum)){
            rlinkPublisher.setAccountNum(new Long(accountNum.trim()));
        }
    }

    public String getAccountNum() {
       return accountNum;
    }

    public void setPubName(String pubName) {
        this.pubName = pubName;
        if ( rlinkPublisher != null){
            rlinkPublisher.setPubName(pubName);
        }
    }

    public String getPubName() {
        return pubName;
    }

    public void setPubUrl(String pubURL) {
        this.pubUrl = pubURL;
        if ( rlinkPublisher != null){
            rlinkPublisher.setPubUrl(pubURL);
        }
    }

    public String getPubUrl() {
        return pubUrl;
    }

    public void setPermOptionDesc(String permOptionDesc) {
        this.permOptionDesc = permOptionDesc;
        if ( rlinkPublisher != null){
            rlinkPublisher.setPermOptionDesc(permOptionDesc);
        }
    }

    public String getPermOptionDesc() {
        return permOptionDesc;
    }

    public void setLearnMoreDesc(String learnMoreDesc) {
        this.learnMoreDesc = learnMoreDesc;
        if ( rlinkPublisher != null){
            rlinkPublisher.setLearnMoreDesc(learnMoreDesc);
        }
    }

    public String getLearnMoreDesc() {
        return learnMoreDesc;
    }
    
    public List<PublisherDisplay> getSubAccounts(){
        return subAccounts;
    }
    
    public void reset() { 
        accountNum="";
        resetExceptAcccountNumber();
    }
    
    public void resetExceptAcccountNumber() {
        pubName="";
        pubUrl="";
        permOptionDesc="";
        learnMoreDesc="";
        rlinkPublisher = null;
        subAccounts = null;
        subAccounts = new ArrayList<PublisherDisplay>();       
    }
    
    public void resetSubAccounts() {
        subAccounts = new ArrayList<PublisherDisplay>();
    }
        
    
    public void setSelectedSubAccountRow(int selectedRow) {
        this.selectedSubAccountRow = selectedRow;
    }

    public int getSelectedSubAccountRow() {
        return selectedSubAccountRow;
    }
    
    public void setSaveAllowed(boolean saveAllowed) {
        this.saveAllowed = saveAllowed;
    }

    public boolean getSaveAllowed() {
        return saveAllowed;
    }
//********************************* Methods below this line are for the add sub account popup
    public void setAddedSubAccount(RLinkPublisher addedSubAccount) {
        this.addedSubAccount = addedSubAccount;
    }

    public RLinkPublisher getAddedSubAccount() {
        return addedSubAccount;
    }
    
    public String getAddedSubAccountPubName(){
        if (getAddedSubAccount()!= null){
            return getAddedSubAccount().getPubName();
        }
        else {
            return "";
        }
    }

    public void setAddedSubAccountNum(String addedSubAccountNum) {
        this.addedSubAccountNum = addedSubAccountNum;
    }

    public String getAddedSubAccountNum() {
        return addedSubAccountNum;
    }
    
    public void resetSubAccountInfo() {
        setAddedSubAccount(null);
        setAddedSubAccountNum("");
    }

    public void setLastSaved(String lastSaved) {
        this.lastSaved = lastSaved;
    }

    public String getLastSaved() {
        return lastSaved;
    }

    public void setSubAccountSaveAllowed(boolean subAccountSaveAllowed) {
        this.subAccountSaveAllowed = subAccountSaveAllowed;
    }

    public boolean getSubAccountSaveAllowed() {
        return subAccountSaveAllowed;
    }
}
