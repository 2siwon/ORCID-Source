import { NgFor, NgIf } 
    from '@angular/common'; 

import { AfterViewInit, Component, NgModule, OnInit, ChangeDetectorRef } 
    from '@angular/core';

import { Observable } 
    from 'rxjs/Rx';

import { Subject } 
    from 'rxjs/Subject';

import { Subscription }
    from 'rxjs/Subscription';

import { BlogService } 
    from '../../shared/blog.service.ts';

import * as xml2js from 'xml2js';

@Component({
    selector: 'home-ng2',
    template:  scriptTmpl("home-ng2-template"),
    providers: [BlogService]
})
export class HomeComponent implements OnInit {
    blogFeed: any;
    
    constructor(
        private cdr:ChangeDetectorRef,
        private blogSrvc: BlogService,
    ) {
        this.blogFeed = {};
    }

    convertToJson(data: string): Object {
        let res;
        xml2js.parseString(data, { explicitArray: false }, (error, result) => {
            if (error) {
            throw new Error(error);
            } else {
            res = result;
            }
        });
        return res;
      }


    ngOnInit() {
        console.log('home init test result');
        this.blogSrvc.getBlogFeed("https://localhost/blog/feed").subscribe(
            result => {
                this.blogFeed=this.convertToJson(result);
                this.cdr.detectChanges(); 
            }
        );
    }
}