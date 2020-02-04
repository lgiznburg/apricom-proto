package ru.apricom.testapp.entities.catalogs;

import ru.apricom.testapp.entities.base.AdmissionCampaign;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue( "CAMPAIGN_TYPE" )
public class AdmissionCampaignType extends BaseCatalog {
    private static final long serialVersionUID = 1589482241928802437L;

    @OneToMany( mappedBy = "type" )
    private List<AdmissionCampaign> campaigns;

    public AdmissionCampaignType() {}
    public AdmissionCampaignType(int code, String title, String shortTitle) {
        super(code, title, shortTitle);
    }

    public List<AdmissionCampaign> getCampaigns() { return campaigns; }

    public void setCampaigns(List<AdmissionCampaign> campaigns) { this.campaigns = campaigns; }
}
