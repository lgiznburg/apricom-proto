package ru.apricom.testapp.encoders;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.util.AbstractSelectModel;
import ru.apricom.testapp.entities.base.AdmissionCampaign;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leonid.
 */
public class AdmissionCampaignsModel extends AbstractSelectModel {
    private List<AdmissionCampaign> campaigns;

    public AdmissionCampaignsModel(List<AdmissionCampaign> campaigns) {
        this.campaigns = campaigns;
    }

    @Override
    public List<OptionGroupModel> getOptionGroups() {
        return null;
    }

    @Override
    public List<OptionModel> getOptions() {
        List<OptionModel> models = new ArrayList<>();
        for ( AdmissionCampaign campaign : campaigns) {
            models.add( new OptionModelImpl( campaign.getName(), campaign ) );
        }
        return models;
    }
}