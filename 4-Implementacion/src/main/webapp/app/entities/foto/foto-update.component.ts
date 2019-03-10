import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IFoto } from 'app/shared/model/foto.model';
import { FotoService } from './foto.service';
import { IAlbum } from 'app/shared/model/album.model';
import { AlbumService } from 'app/entities/album';

@Component({
    selector: 'jhi-foto-update',
    templateUrl: './foto-update.component.html'
})
export class FotoUpdateComponent implements OnInit {
    foto: IFoto;
    isSaving: boolean;

    albums: IAlbum[];

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected fotoService: FotoService,
        protected albumService: AlbumService,
        protected elementRef: ElementRef,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ foto }) => {
            this.foto = foto;
        });
        this.albumService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IAlbum[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAlbum[]>) => response.body)
            )
            .subscribe((res: IAlbum[]) => (this.albums = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.foto, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.foto.id !== undefined) {
            this.subscribeToSaveResponse(this.fotoService.update(this.foto));
        } else {
            this.subscribeToSaveResponse(this.fotoService.create(this.foto));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IFoto>>) {
        result.subscribe((res: HttpResponse<IFoto>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackAlbumById(index: number, item: IAlbum) {
        return item.id;
    }
}
