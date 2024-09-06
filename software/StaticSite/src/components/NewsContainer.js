import React from 'react';
import {Pagination} from "./Pagination";

export const NewsContainer = ({children}) => {
    return (
        <div className="col">
            <div className="container-fluid w-90 text-center">
                <div className="row shadow">
                    <div className="col-auto w-250">
                        {children[0]}
                    </div>
                    <div className="col">
                        {children[1]}
                    </div>
                </div>
            </div>
        </div>
    );
};