<#--

    =============================================================================

    ORCID (R) Open Source
    http://orcid.org

    Copyright (c) 2012-2014 ORCID, Inc.
    Licensed under an MIT-Style License (MIT)
    http://orcid.org/open-source-license

    This copyright and license information (including a link to the full license)
    shall be included in its entirety in all copies or substantial portion of
    the software.

    =============================================================================

-->
<#import "email_macros.ftl" as emailMacros />
We are writing to let you know about a bug that we identified recently, which may have affected the display of one or more of the organization names in your ORCID record.
As a result, your record may currently include incorrect information about the following organization(s):

<#list orgDescriptions as orgDescription>
     ${orgDescription}
</#list>

We have fixed the bug and have tried to fix the affiliation data that was affected in your record, though some correction may still be needed. We have changed the visibility setting on affected information to private - visible only to you - to allow you to review changes. We encourage you to sign into your record at https://orcid.org/my-orcid to review and, if necessary, correct the affected affiliation information. You can then decide whether to keep this information visible only to you, make it publicly available, or share it only with those you trust.

We apologize for any inconvenience this may cause you. If you have any questions or concerns, or need information about how to update your record and/or the visibility settings, our global support team are available to help you, at support@orcid.org. 

Regards,

Laure Haak
Executive Director, ORCID
laure@orcid.org

${baseUri}

<#include "email_footer.ftl"/>