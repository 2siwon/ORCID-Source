/**
 * =============================================================================
 *
 * ORCID (R) Open Source
 * http://orcid.org
 *
 * Copyright (c) 2012-2013 ORCID, Inc.
 * Licensed under an MIT-Style License (MIT)
 * http://orcid.org/open-source-license
 *
 * This copyright and license information (including a link to the full license)
 * shall be included in its entirety in all copies or substantial portion of
 * the software.
 *
 * =============================================================================
 */
package org.orcid.pojo.ajaxForm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.orcid.core.adapter.impl.Jpa2JaxbAdapterImpl;
import org.orcid.core.security.visibility.OrcidVisibilityDefaults;
import org.orcid.core.utils.JsonUtils;
import org.orcid.jaxb.model.message.CitationType;
import org.orcid.jaxb.model.message.Country;
import org.orcid.jaxb.model.message.FuzzyDate;
import org.orcid.jaxb.model.message.Iso3166Country;
import org.orcid.jaxb.model.message.OrcidProfile;
import org.orcid.jaxb.model.message.OrcidType;
import org.orcid.jaxb.model.message.OrcidWork;
import org.orcid.jaxb.model.message.PublicationDate;
import org.orcid.jaxb.model.message.Title;
import org.orcid.jaxb.model.message.Url;
import org.orcid.jaxb.model.message.Visibility;
import org.orcid.jaxb.model.message.WorkContributors;
import org.orcid.jaxb.model.message.WorkExternalIdentifiers;
import org.orcid.jaxb.model.message.WorkSource;
import org.orcid.jaxb.model.message.WorkType;
import org.orcid.persistence.jpa.entities.ProfileEntity;
import org.orcid.persistence.jpa.entities.ProfileWorkEntity;
import org.orcid.persistence.jpa.entities.WorkEntity;
import org.orcid.persistence.jpa.entities.custom.MinimizedWorkEntity;

public class Work implements ErrorsInterface, Serializable {

	public static final String OTHER_AT_BOTTOM = "zzz-other";
	
	private static final long serialVersionUID = 1L;

	private List<String> errors = new ArrayList<String>();

	private Date publicationDate;

	private Visibility visibility;

	private Text putCode;

	private Text shortDescription;

	private Text url;

	private Text journalTitle;

	private Text languageCode;

	private Text languageName;

	private Citation citation;

	private Text countryCode;

	private Text countryName;

	private List<Contributor> contributors;

	private List<WorkExternalIdentifier> workExternalIdentifiers;

	private Text workSource;

	private Text workSourceName;

	private WorkTitle workTitle;

	private Text workCategory;

	private Text workType;

	protected String citationForDisplay;
	
	private String dateSortString;
	

	public static Work valueOf(MinimizedWorkEntity minimizedWorkEntity) {
		Work w = new Work();
		// Set id
		w.setPutCode(Text.valueOf(String.valueOf(minimizedWorkEntity.getId())));
		// Set publication date
		Integer year = (minimizedWorkEntity.getPublicationYear() <= 0) ? null
				: minimizedWorkEntity.getPublicationYear();
		Integer month = (minimizedWorkEntity.getPublicationMonth() <= 0) ? null
				: minimizedWorkEntity.getPublicationMonth();
		Integer day = (minimizedWorkEntity.getPublicationDay() <= 0) ? null
				: minimizedWorkEntity.getPublicationDay();
		FuzzyDate fuzz = new FuzzyDate(year, month, day);
		w.setPublicationDate(Date.valueOf(fuzz));
		w.setDateSortString(PojoUtil.createDateSortString(null, fuzz));
                
		// Set title and subtitle
		if (!StringUtils.isEmpty(minimizedWorkEntity.getTitle())) {
			WorkTitle workTitle = new WorkTitle();
			Text title = Text.valueOf(minimizedWorkEntity.getTitle());
			workTitle.setTitle(title);
			if (!StringUtils.isEmpty(minimizedWorkEntity.getSubtitle())) {
				Text subtitle = Text.valueOf(minimizedWorkEntity.getSubtitle());
				workTitle.setSubtitle(subtitle);
			}
			w.setWorkTitle(workTitle);
		}
		// Set description
		if (!StringUtils.isEmpty(minimizedWorkEntity.getDescription())) {
			w.setShortDescription(Text.valueOf(minimizedWorkEntity
					.getDescription()));
		}
		// Set visibility
		if (minimizedWorkEntity.getVisibility() != null)
			w.setVisibility(minimizedWorkEntity.getVisibility());

		if (!StringUtils.isEmpty(minimizedWorkEntity.getExternalIdentifiersJson())) {
		    WorkExternalIdentifiers identifiers = JsonUtils.readObjectFromJsonString(minimizedWorkEntity.getExternalIdentifiersJson(), WorkExternalIdentifiers.class);
		    populateExternaIdentifiers(identifiers, w);            
		}
		if (minimizedWorkEntity.getSourceProfile() != null) {
                    w.setWorkSource(Text.valueOf(minimizedWorkEntity.getSourceProfile().getId()));
                    w.setWorkSourceName(Text.valueOf(Jpa2JaxbAdapterImpl.createName(minimizedWorkEntity.getSourceProfile())));
                }
		return w;
	}
	
	public static Work valueOf(OrcidWork orcidWork) {
		Work w = new Work();
		if (orcidWork.getPublicationDate() != null) 
		    w.setPublicationDate(Date.valueOf(orcidWork.getPublicationDate()));
		w.setDateSortString(PojoUtil.createDateSortString(null, orcidWork.getPublicationDate()));
		if (orcidWork.getPutCode() != null)
			w.setPutCode(Text.valueOf(orcidWork.getPutCode()));
		if (orcidWork.getShortDescription() != null)
			w.setShortDescription(Text.valueOf(orcidWork.getShortDescription()));
		if (orcidWork.getUrl() != null)
			w.setUrl(Text.valueOf(orcidWork.getUrl().getValue()));
		if (orcidWork.getVisibility() != null)
			w.setVisibility(orcidWork.getVisibility());
		if (orcidWork.getWorkCitation() != null)
			w.setCitation(Citation.valueOf(orcidWork.getWorkCitation()));

		if (orcidWork.getWorkContributors() != null
				&& orcidWork.getWorkContributors().getContributor() != null) {
			List<Contributor> contributors = new ArrayList<Contributor>();
			for (org.orcid.jaxb.model.message.Contributor owContributor : orcidWork
					.getWorkContributors().getContributor()) {
				contributors.add(Contributor.valueOf(owContributor));
			}
			w.setContributors(contributors);
		}
		if (orcidWork.getWorkExternalIdentifiers() != null) {
		    WorkExternalIdentifiers workExternalIdentifiers = orcidWork.getWorkExternalIdentifiers();
		    populateExternaIdentifiers(workExternalIdentifiers, w);
		}
		if (orcidWork.getWorkSource() != null) {
			w.setWorkSource(Text.valueOf(orcidWork.getWorkSource().getPath()));
			if (orcidWork.getWorkSource().getSourceName() != null)
				w.setWorkSourceName(Text.valueOf(orcidWork.getWorkSource()
						.getSourceName()));
		}
		if (orcidWork.getWorkTitle() != null)
			w.setWorkTitle(WorkTitle.valueOf(orcidWork.getWorkTitle()));
		if (orcidWork.getWorkType() != null)
			w.setWorkType(Text.valueOf(orcidWork.getWorkType().value()));

		if (orcidWork.getJournalTitle() != null)
			w.setJournalTitle(Text.valueOf(orcidWork.getJournalTitle()
					.getContent()));

		if (orcidWork.getLanguageCode() != null)
			w.setLanguageCode(Text.valueOf(orcidWork.getLanguageCode()));

		if (orcidWork.getCountry() != null)
			w.setCountryCode((orcidWork.getCountry().getValue() == null) ? null
					: Text.valueOf(orcidWork.getCountry().getValue().value()));
		return w;
	}

    private static void populateExternaIdentifiers(WorkExternalIdentifiers workExternalIdentifiers, Work work) {
        if (workExternalIdentifiers.getWorkExternalIdentifier() != null) {
        List<WorkExternalIdentifier> workExternalIdentifiersList = new ArrayList<WorkExternalIdentifier>();
        for (org.orcid.jaxb.model.message.WorkExternalIdentifier owWorkExternalIdentifier : workExternalIdentifiers.getWorkExternalIdentifier()) {
        	workExternalIdentifiersList.add(WorkExternalIdentifier
        			.valueOf(owWorkExternalIdentifier));
        }
        work.setWorkExternalIdentifiers(workExternalIdentifiersList);
        }
    }
	
	public static Work minimizedValueOf(OrcidWork orcidWork) {
		Work w = new Work();
		if (orcidWork.getPublicationDate() != null)
			w.setPublicationDate(Date.valueOf(orcidWork.getPublicationDate()));
		w.setDateSortString(PojoUtil.createDateSortString(null, orcidWork.getPublicationDate()));
		if (orcidWork.getPutCode() != null)
			w.setPutCode(Text.valueOf(orcidWork.getPutCode()));
		if (orcidWork.getShortDescription() != null)
			w.setShortDescription(Text.valueOf(orcidWork.getShortDescription()));
		if (orcidWork.getVisibility() != null)
			w.setVisibility(orcidWork.getVisibility());
		if (orcidWork.getWorkTitle() != null)
			w.setWorkTitle(WorkTitle.valueOf(orcidWork.getWorkTitle()));
		return w;
	}

	public OrcidWork toOrcidWork() {
		OrcidWork ow = new OrcidWork();
		if (this.getPublicationDate() != null)
			ow.setPublicationDate(new PublicationDate(this.getPublicationDate()
					.toFuzzyDate()));
		if (this.getPutCode() != null)
			ow.setPutCode(this.getPutCode().getValue());
		if (this.getShortDescription() != null)
			ow.setShortDescription(this.shortDescription.getValue());
		if (this.getUrl() != null)
			ow.setUrl(new Url(this.url.getValue()));
		if (this.getVisibility() != null)
			ow.setVisibility(this.getVisibility());
		if (this.getCitation() != null)
			ow.setWorkCitation(this.citation.toCitiation());
		if (this.getContributors() != null) {
			List<org.orcid.jaxb.model.message.Contributor> cList = new ArrayList<org.orcid.jaxb.model.message.Contributor>();
			for (Contributor c : this.getContributors()) {
				cList.add(c.toContributor());
			}
			ow.setWorkContributors(new WorkContributors(cList));
		}
		if (this.getWorkExternalIdentifiers() != null) {
			List<org.orcid.jaxb.model.message.WorkExternalIdentifier> wiList = new ArrayList<org.orcid.jaxb.model.message.WorkExternalIdentifier>();
			for (WorkExternalIdentifier wi : this.getWorkExternalIdentifiers()) {
				wiList.add(wi.toWorkExternalIdentifier());
			}
			ow.setWorkExternalIdentifiers(new WorkExternalIdentifiers(wiList));
		}
		if (this.getWorkSource() != null)
			ow.setWorkSource(new WorkSource(this.getWorkSource().getValue()));
		if (this.getWorkTitle() != null) {
			ow.setWorkTitle(this.workTitle.toWorkTitle());
		}
		if (this.getWorkType() != null) {
			if(!OTHER_AT_BOTTOM.equals(this.getWorkType().getValue()))
				ow.setWorkType(WorkType.fromValue(this.getWorkType().getValue()));				
			else
				ow.setWorkType(WorkType.OTHER);
		}

		if (this.getJournalTitle() != null) {
			ow.setJournalTitle(new Title(this.getJournalTitle().getValue()));
		}

		if (this.getLanguageCode() != null) {
			ow.setLanguageCode(this.getLanguageCode().getValue());
		}

		if (this.getCountryCode() != null) {
			Country country = new Country(
					StringUtils.isEmpty(this.getCountryCode().getValue()) ? null
							: Iso3166Country.fromValue(this.getCountryCode()
									.getValue()));
			ow.setCountry(country);
		}

		return ow;
	}

	public void setCitationForDisplay(String citation) {
		this.citationForDisplay = citation;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public Text getPutCode() {
		return putCode;
	}

	public void setPutCode(Text putCode) {
		this.putCode = putCode;
	}

	public Text getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(Text shortDescription) {
		this.shortDescription = shortDescription;
	}

	public Text getUrl() {
		return url;
	}

	public void setUrl(Text url) {
		this.url = url;
	}

	public Citation getCitation() {
		return citation;
	}

	public void setCitation(Citation citation) {
		this.citation = citation;
	}

	public List<Contributor> getContributors() {
		return contributors;
	}

	public void setContributors(List<Contributor> contributors) {
		this.contributors = contributors;
	}

	public List<WorkExternalIdentifier> getWorkExternalIdentifiers() {
		return workExternalIdentifiers;
	}

	public void setWorkExternalIdentifiers(
			List<WorkExternalIdentifier> workExternalIdentifiers) {
		this.workExternalIdentifiers = workExternalIdentifiers;
	}

	public Text getWorkSource() {
		return workSource;
	}

	public void setWorkSource(Text workSource) {
		this.workSource = workSource;
	}

	public WorkTitle getWorkTitle() {
		return workTitle;
	}

	public void setWorkTitle(WorkTitle worksTitle) {
		this.workTitle = worksTitle;
	}

	public Text getWorkType() {
		return workType;
	}

	public void setWorkType(Text workType) {
		this.workType = workType;
	}

	public Text getWorkCategory() {
		return workCategory;
	}

	public void setWorkCategory(Text workCategory) {
		this.workCategory = workCategory;
	}

	public Text getJournalTitle() {
		return journalTitle;
	}

	public void setJournalTitle(Text journalTitle) {
		this.journalTitle = journalTitle;
	}

	public Text getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(Text languageCode) {
		this.languageCode = languageCode;
	}

	public Text getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(Text countryCode) {
		this.countryCode = countryCode;
	}

	public Text getLanguageName() {
		return languageName;
	}

	public void setLanguageName(Text languageName) {
		this.languageName = languageName;
	}

	public Text getCountryName() {
		return countryName;
	}

	public void setCountryName(Text countryName) {
		this.countryName = countryName;
	}

	public Text getWorkSourceName() {
		return workSourceName;
	}

	public void setWorkSourceName(Text workSourceName) {
		this.workSourceName = workSourceName;
	}

    public String getDateSortString() {
        return dateSortString;
    }

    public void setDateSortString(String dateSortString) {
        this.dateSortString = dateSortString;
    }
}