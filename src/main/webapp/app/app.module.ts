import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { ChattySharedModule } from 'app/shared/shared.module';
import { ChattyCoreModule } from 'app/core/core.module';
import { ChattyAppRoutingModule } from './app-routing.module';
import { ChattyHomeModule } from './home/home.module';
import { ChattyEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    ChattySharedModule,
    ChattyCoreModule,
    ChattyHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    ChattyEntityModule,
    ChattyAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class ChattyAppModule {}
