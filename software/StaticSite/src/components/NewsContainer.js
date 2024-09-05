import React from 'react';
import {Pagination} from "./Pagination";

export const NewsContainer = ({children}) => {
    return (
        <div className="row text-center pb-3" data-bs-theme="dark">
            <div className="col">
                <div className="container-fluid w-90">
                    <div className="row">
                        <div className="col-2 px-0">
                            {children[0]}
                        </div>
                        <div className="col ms-4 ps-0">
                            {children[1]}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};