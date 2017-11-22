import { Injectable, Pipe, PipeTransform } 
    from '@angular/core';

@Pipe({
    name: "orgIdentifierHtml"
})

@Injectable()
export class OrgIdentifierHtmlPipe implements PipeTransform {
    transform(value: any, type: any, putCode: any): any {
        var GRID_BASE_URL = "https://www.grid.ac/institutes/";
        var TEST_BASE_URL = "https://orcid.org/";
        var link = null;
        var output = '';       

        if (value == null) {
            return output;
        }

        //disambiguation-source is not enumerated; using types that currently exist in orcid db
        if (type != null) {
            if (type == 'TEST') {
                link = TEST_BASE_URL + value;
                output += 'Test Id: ';
            } else if (type == 'FUNDREF') {
                link = value;
                output += om.get('affiliation.org_id.value.label.fundref') + ": </div>";
            } else if (type == 'GRID') {
                link = GRID_BASE_URL + value;
                output += om.get('affiliation.org_id.value.label.grid') + ": ";
            } else if (type == 'RINGGOLD') {
                output += om.get('affiliation.org_id.value.label.ringgold') + ": ";
            } else {
                link = null;
                output += type + ": ";
            }
            
        }         
        
        if(link != null) {
            if(value != null) {
                output += '<a href=' + link + " class='truncate-anchor' target='orcid.blank' ng-mouseenter='showURLPopOver(" + putCode + ")' ng-mouseleave='hideURLPopOver(" + putCode + ")'>" + value.escapeHtml() + "</a>";
            }
        } else if(value != null) {
            output = output + ' ' + value.escapeHtml();
        }
        
        if( link != null ) {            
            output += '<div class="popover-pos">\
                        <div class="popover-help-container">\
                            <div class="popover bottom" ng-class="{'+"'block'"+' : displayURLPopOver[' + putCode + '] == true}">\
                                <div class="arrow"></div>\
                                <div class="popover-content">\
                                    <a href="'+link+'" target="orcid.blank" class="ng-binding">'+link.escapeHtml()+'</a>\
                                </div>\
                            </div>\
                        </div>\
                        </div>\
                  </div>';
        }
        
        return output;
    }
}
