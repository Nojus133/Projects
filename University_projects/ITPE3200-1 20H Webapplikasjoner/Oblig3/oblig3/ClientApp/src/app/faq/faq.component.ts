import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { Faq } from '../Faq';

@Component({
  selector: 'app-faq',
  templateUrl: './faq.component.html',
  styleUrls: ['./faq.component.scss']
})
export class FaqComponent {
  alleFaq: Array<Faq>;
  laster: boolean = true;
  alleKategorier: Array<string>;
  isOpen: boolean = false;
  skjema: FormGroup;
  loading: boolean = false;
  err: String;

  validering = {
    spors: [
      null,Validators.compose([Validators.required, Validators.maxLength(150), Validators.minLength(15)])
    ],
    kategori: [
      null,Validators.compose([Validators.required])
    ]
  }

  constructor(private _http: HttpClient, private fb: FormBuilder, private ref: ChangeDetectorRef) {
    this.skjema = fb.group(this.validering);
  }

  ngOnInit() {
    this.hentAlleFaq();
  }

  lagreFaq() {
    this.loading = true;
    const nySporsmaal = new Faq();
    nySporsmaal.kategori = this.skjema.value.kategori;
    nySporsmaal.spors = this.skjema.value.spors;
    nySporsmaal.svar = "";
    nySporsmaal.rating = 0;
    this._http.post<any>("api/faq/", nySporsmaal).subscribe(retur => {
            this.hentAlleFaq();
            this.loading = false;
            this.isOpen = !this.isOpen;
          },
        error => {
          console.log(error);
          this.err = error.message;
          this.loading = false;
        },
        () => console.log("ferdig post-api/faq")
    );
    
  }

  hentAlleFaq() {
    this._http.get<Faq[]>("api/faq/")
        .subscribe(svarene => {
            
              this.alleFaq = svarene;
            this.alleKategorier = this.getAlleKategorier(svarene);
            this.laster = false;
            
        },
        error => console.log(error),
        () => console.log("ferdig get-api/faq")
    );
  };

  getAlleKategorier(liste) {
    return liste.map(e => e.kategori.kategorinavn)
    .filter((currentValue, index, array) => array.indexOf(currentValue) === index);
  }

  toggleModal(e) {
    this.skjema.setValue({
      kategori: "",
      spors: "",
    });
    this.err = "";
    this.skjema.markAsPristine();
    e.currentTarget.blur();
    this.isOpen = !this.isOpen;
  }
}