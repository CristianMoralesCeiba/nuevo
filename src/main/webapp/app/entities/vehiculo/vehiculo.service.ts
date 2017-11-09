import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Vehiculo } from './vehiculo.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class VehiculoService {

    private resourceUrl = SERVER_API_URL + 'api/vehiculos';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(vehiculo: Vehiculo): Observable<Vehiculo> {
        const copy = this.convert(vehiculo);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(vehiculo: Vehiculo): Observable<Vehiculo> {
        const copy = this.convert(vehiculo);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Vehiculo> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Vehiculo.
     */
    private convertItemFromServer(json: any): Vehiculo {
        const entity: Vehiculo = Object.assign(new Vehiculo(), json);
        entity.fechaIngreso = this.dateUtils
            .convertLocalDateFromServer(json.fechaIngreso);
        return entity;
    }

    /**
     * Convert a Vehiculo to a JSON which can be sent to the server.
     */
    private convert(vehiculo: Vehiculo): Vehiculo {
        const copy: Vehiculo = Object.assign({}, vehiculo);
        copy.fechaIngreso = this.dateUtils
            .convertLocalDateToServer(vehiculo.fechaIngreso);
        return copy;
    }
}
