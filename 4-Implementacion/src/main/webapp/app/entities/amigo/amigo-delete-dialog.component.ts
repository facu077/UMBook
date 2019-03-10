import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAmigo } from 'app/shared/model/amigo.model';
import { AmigoService } from './amigo.service';

@Component({
    selector: 'jhi-amigo-delete-dialog',
    templateUrl: './amigo-delete-dialog.component.html'
})
export class AmigoDeleteDialogComponent {
    amigo: IAmigo;

    constructor(protected amigoService: AmigoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.amigoService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'amigoListModification',
                content: 'Deleted an amigo'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-amigo-delete-popup',
    template: ''
})
export class AmigoDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ amigo }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AmigoDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.amigo = amigo;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/amigo', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/amigo', { outlets: { popup: null } }]);
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
