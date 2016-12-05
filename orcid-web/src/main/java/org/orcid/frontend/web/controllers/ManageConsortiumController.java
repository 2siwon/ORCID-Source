package org.orcid.frontend.web.controllers;

import java.util.List;

import javax.annotation.Resource;

import org.orcid.core.exception.OrcidForbiddenException;
import org.orcid.core.exception.OrcidUnauthorizedException;
import org.orcid.core.manager.SalesForceManager;
import org.orcid.core.oauth.service.OrcidAuthorizationEndpoint;
import org.orcid.core.salesforce.model.Contact;
import org.orcid.core.salesforce.model.Member;
import org.orcid.core.salesforce.model.MemberDetails;
import org.orcid.pojo.ajaxForm.ConsortiumForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author Will Simpson
 *
 */
@Controller
@RequestMapping(value = { "/manage-consortium" })
public class ManageConsortiumController extends BaseController {

    @Resource
    private SalesForceManager salesForceManager;

    @RequestMapping
    public ModelAndView getManageConsortiumPage() {
        ModelAndView mav = new ModelAndView("manage_consortium");
        return mav;
    }

    @RequestMapping(value = "/get-consortium.json", method = RequestMethod.GET)
    public @ResponseBody ConsortiumForm getConsortium() {
        String accountId = salesForceManager.retriveAccountIdByOrcid(getCurrentUserOrcid());
        MemberDetails memberDetails = salesForceManager.retrieveDetails(accountId);
        ConsortiumForm consortiumForm = ConsortiumForm.fromMemberDetails(memberDetails);
        List<Contact> contactsList = salesForceManager.retrieveContactsByAccountId(accountId);
        salesForceManager.addOrcidsToContacts(contactsList);
        consortiumForm.setContactsList(contactsList);
        return consortiumForm;
    }

    @RequestMapping(value = "/update-consortium.json", method = RequestMethod.POST)
    public @ResponseBody ConsortiumForm updateConsortium(@RequestBody ConsortiumForm consortium) {
        MemberDetails memberDetails = consortium.toMemberDetails();
        String usersAuthorizedAccountId = salesForceManager.retriveAccountIdByOrcid(getCurrentUserOrcid());
        Member member = memberDetails.getMember();
        if (!usersAuthorizedAccountId.equals(member.getId())) {
            throw new OrcidUnauthorizedException("You are not authorized for account ID = " + member.getId());
        }
        salesForceManager.updateMember(member);
        return consortium;
    }

}
