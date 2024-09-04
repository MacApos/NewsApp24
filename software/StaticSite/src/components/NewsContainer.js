import React from 'react';
import {Sort} from "./Sort";

export const NewsContainer = (props) => {
    return (
        <div className="row text-center pb-3" data-bs-theme="dark">
            <div className="col">
                <div className="container-fluid w-90">
                    <div className="row">
                        <div className="col-2 px-0">
                            {props.children[0]}
                        </div>
                        <div className="col bg-success">
                            {props.children[1]}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};