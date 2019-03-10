import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFoto } from 'app/shared/model/foto.model';
import { FotoService } from './foto.service';

@Component({
    selector: 'jhi-foto-delete-dialog',
    templateUrl: './foto-delete-dialog.component.html'
})
export class FotoDeleteDialogComponent {
    foto: IFoto;

    constructor(protected fotoService: FotoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.fotoService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'fotoListModification',
                content: 'Deleted an foto'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-foto-delete-popup',
    template: ''
})
export class FotoDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ foto }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FotoDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.foto = foto;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/foto', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/foto', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
