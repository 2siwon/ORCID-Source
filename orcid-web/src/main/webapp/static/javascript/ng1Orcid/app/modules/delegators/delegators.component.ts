declare var $: any;
declare var getBaseUri: any;
declare var logAjaxError: any;
declare var om: any;
//Import all the angular components

import { NgFor, NgIf } 
    from '@angular/common'; 

import { AfterViewInit, Component, OnDestroy, OnInit } 
    from '@angular/core';

import { Observable } 
    from 'rxjs/Rx';

import { Subject } 
    from 'rxjs/Subject';

import { Subscription }
    from 'rxjs/Subscription';

import { DelegatorsService } 
    from '../../shared/delegators.service.ts'; 


@Component({
    selector: 'delegators-ng2',
    template:  scriptTmpl("delegators-ng2-template")
})
export class DelegatorsComponent implements AfterViewInit, OnDestroy, OnInit {
    private ngUnsubscribe: Subject<void> = new Subject<void>();
   
    delegators: any;
    sort: any;

    constructor(
        private delegatorsService: DelegatorsService
    ) {
        this.sort = {
            column: 'delegateSummary.giverName.value',
            descending: false
        };
        this.delegators = {};

    }

    changeSorting(column): void {
        var sort = this.sort;
        if (sort.column === column) {
            sort.descending = !sort.descending;
        } else {
            sort.column = column;
            sort.descending = false;
        }
    };

    getDelegators(): void {
        this.delegatorsService.getDelegators()
        .takeUntil(this.ngUnsubscribe)
        .subscribe(
            data => {
                this.delegators = data.delegators;
            },
            error => {
                console.log('setformDataError', error);
                logAjaxError(error);
            } 
        );

    };

    selectDelegator(datum): void {
        window.location.href = getBaseUri() + '/switch-user?username=' + datum.orcid;
    };

    
    //Default init functions provided by Angular Core
    ngAfterViewInit() {
        //Fire functions AFTER the view inited. Useful when DOM is required or access children directives
    };

    ngOnDestroy() {
        this.ngUnsubscribe.next();
        this.ngUnsubscribe.complete();
    };

    ngOnInit() {
        this.getDelegators();

        (<any>$("#delegatorsSearch")).typeahead({
            name: 'delegatorsSearch',
            remote: {
                url: getBaseUri()+'/delegators/search-for-data/%QUERY?limit=' + 10
            },
            template: function (datum) {
                var forDisplay;
                if(datum.length == 0){
                    forDisplay = "<span class=\'no-delegator-matches\'>" + om.get('delegators.nomatches') + "</span>";
                }
                else{
                    forDisplay =
                        '<span style=\'white-space: nowrap; font-weight: bold;\'>' + datum.value + '</span>'
                        +'<span style=\'font-size: 80%;\'> (' + datum.orcid + ')</span>';
                }
                return forDisplay;
            }
        });

        $("#delegatorsSearch").bind(
            "typeahead:selected", 
            function(obj, datum) {
                if(datum.orcid != null){
                    this.selectDelegator(datum);
                }
            }
        );
    }; 
}
