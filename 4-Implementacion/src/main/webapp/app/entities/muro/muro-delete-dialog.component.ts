import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMuro } from 'app/shared/model/muro.model';
import { MuroService } from './muro.service';

@Component({
    selector: 'jhi-muro-delete-dialog',
    templateUrl: './muro-delete-dialog.component.html'
})
export class MuroDeleteDialogComponent {
    muro: IMuro;

    constructor(protected muroService: MuroService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.muroService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'muroListModification',
                content: 'Deleted an muro'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-muro-delete-popup',
    template: ''
})
export class MuroDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ muro }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MuroDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.muro = muro;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/muro', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/muro', { outlets: { popup: null } }]);
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
