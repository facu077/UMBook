import { NgModule } from '@angular/core';

import { UmbookSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [UmbookSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [UmbookSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class UmbookSharedCommonModule {}
