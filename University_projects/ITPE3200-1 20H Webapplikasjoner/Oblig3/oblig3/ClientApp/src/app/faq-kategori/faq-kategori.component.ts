import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Faq } from '../Faq';

@Component({
  selector: 'app-faq-kategori',
  templateUrl: './faq-kategori.component.html',
  styleUrls: ['./faq-kategori.component.scss']
})
export class FaqKategoriComponent implements OnChanges, OnInit {
    @Input() kategori: string;
    @Input() alleFaq: Array<any>;
    svarene: Array<any>;
    utenSvar: Array<any>;
    medSvar: Array<any>;

    isExpanded: boolean = false;
    isExpandedSpors: boolean = false;

    ngOnChanges(changes: SimpleChanges) {
        this.alleFaq = changes.alleFaq.currentValue;
        this.hentFaq();
    } 

    ngOnInit() {
        this.hentFaq();
    }

    

    hentFaq() {
        this.svarene = this.alleFaq.filter(e => e.kategori.kategorinavn === this.kategori);
        this.utenSvar = this.svarene.filter(e => e.svar === "");
        this.medSvar = this.svarene.filter(e => e.svar !== "");
    }

    toggle(e) {
        e.currentTarget.blur();
        this.isExpanded = !this.isExpanded;
    }

    toggleSporsmaal(e) {
        e.currentTarget.blur();
        this.isExpandedSpors = !this.isExpandedSpors;
    }
}