import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import {CommonModule} from "@angular/common"

import { AppComponent } from './app.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { FaqComponent } from './faq/faq.component';
import { FaqKategoriComponent } from './faq-kategori/faq-kategori.component';
import { FaqItemComponent } from './faq-item/faq-item.component';

@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent,
    FaqComponent,
    FaqKategoriComponent, 
    FaqItemComponent
  ],
  imports: [
    BrowserModule.withServerTransition({ appId: 'ng-cli-universal' }),
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
