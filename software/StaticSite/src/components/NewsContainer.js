import React from 'react';
import {Pagination} from "./Pagination";

export const NewsContainer = ({children}) => {
    return (
        <div className="row text-center pb-4" data-bs-theme="dark">
            <div className="col">
                <div className="container-fluid w-90 text-center">
                    <div className="row shadow">
                        <div className="col-auto col-w-275">
                            {children[0]}
                        </div>
                        <div className="col">
                            {children[1]}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};