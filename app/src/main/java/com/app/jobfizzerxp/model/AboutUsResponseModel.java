
package com.app.jobfizzerxp.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AboutUsResponseModel implements Serializable {

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("page")
    @Expose
    private List<Page> page = null;
    private final static long serialVersionUID = 8039303287397831656L;

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<Page> getPage() {
        return page;
    }

    public void setPage(List<Page> page) {
        this.page = page;
    }

    public class Page implements Serializable {

        @SerializedName("privacyPolicy")
        @Expose
        private String privacyPolicy;
        @SerializedName("termsAndCondition")
        @Expose
        private String termsAndCondition;
        @SerializedName("privacyPolicyContent")
        @Expose
        private String privacyPolicyContent;
        @SerializedName("termsAndConditionContent")
        @Expose
        private String termsAndConditionContent;
        private final static long serialVersionUID = 7090338597169978389L;

        public String getPrivacyPolicy() {
            return privacyPolicy;
        }

        public void setPrivacyPolicy(String privacyPolicy) {
            this.privacyPolicy = privacyPolicy;
        }

        public String getTermsAndCondition() {
            return termsAndCondition;
        }

        public void setTermsAndCondition(String termsAndCondition) {
            this.termsAndCondition = termsAndCondition;
        }

        public String getPrivacyPolicyContent() {
            return privacyPolicyContent;
        }

        public void setPrivacyPolicyContent(String privacyPolicyContent) {
            this.privacyPolicyContent = privacyPolicyContent;
        }

        public String getTermsAndConditionContent() {
            return termsAndConditionContent;
        }

        public void setTermsAndConditionContent(String termsAndConditionContent) {
            this.termsAndConditionContent = termsAndConditionContent;
        }

    }

}
