package com.copyright.ccc.web.forms.admin;

import java.util.ArrayList;
import java.util.List;

import com.copyright.ccc.business.data.RLinkPublisher;
import com.copyright.ccc.web.CCValidatorForm;
import com.copyright.ccc.web.util.PublisherDisplay;

public class PublisherMaintenanceForm extends CCValidatorForm {
    
	private static final long serialVersionUID = 1L;
	
    private List<PublisherDisplay> publishersDisplay = new ArrayList<PublisherDisplay>(); 
    private int numSelectedRows;
    private int colIndex;
    private int selectedRow;
    private String [] selectedPublishers = new String[25];
    
    public PublisherMaintenanceForm() {
    }

    public void setPublishersDisplay(RLinkPublisher[] rlPublishers) {
        //translate collection publishers into collection with boolean for noting which rows selected
         publishersDisplay = new ArrayList<PublisherDisplay>();
        for (RLinkPublisher rlPublisher: rlPublishers) {
            PublisherDisplay publisherDisplay = new PublisherDisplay(rlPublisher,false);
            publishersDisplay.add(publisherDisplay);
        }
    }

    public List<PublisherDisplay> getPublishersDisplay() {
        return publishersDisplay;
    }

    public int getNumSelectedRows() {
        int selectedRows=0;
        for (PublisherDisplay publisherDisplay: publishersDisplay){
            if (publisherDisplay.getSelected()) {
                selectedRows++;
            }
        }
        numSelectedRows=selectedRows;
        return selectedRows;
    }
    
    public PublisherDisplay getPublisher(int index){
        return publishersDisplay.get(index);
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }

    public int getColIndex() {
        return colIndex;
    }
    
    public void setSelected(int selected){
        int rowSelected = selected;
    }

    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public void setSelectedPublishers(String[] selectedPublishers) {
        this.selectedPublishers = selectedPublishers;
    }

    public String[] getSelectedPublishers() {
        return selectedPublishers;
    }
}
