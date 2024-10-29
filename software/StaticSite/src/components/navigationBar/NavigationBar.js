import React from 'react';
import {LOGO} from "../../constants/constants";

export const NavigationBar = ({children}) => {
    return (
        <div className="row text-center bg-primary" data-bs-theme="dark">
            <div className="col">
                <div className="container-fluid w-90">
                    <div className="row align-items-end" id="navbar-row">
                        <div className="col-auto w-250-px navbar-btn">
                            <div className="row navbar py-3" data-bs-theme="dark">
                                <div className="col">
                                    <span className="navbar-brand text-capitalize fw-bolder fs-3">
                                        <img src={LOGO} alt="Logo" width="35"
                                             height="auto" className="d-inline-block align-text-top "/>
                                        &nbsp;NewsApp24
                                    </span>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col">
                                    {children[0]}
                                </div>
                            </div>
                        </div>
                        <div className="col">
                            <div className="row justify-content-between align-items-center" id="navbar-sub-row">
                                <div className="col-auto navbar-btn w-500-px order-1" id="col-order-1">
                                    <div className="row align-items-center">
                                        <div className="col-auto navbar-div text-end">
                                            Sort
                                        </div>
                                        <div className="col">
                                            {children[1]}
                                        </div>
                                        <div className="col navbar-div text-end">
                                            Articles on page
                                        </div>
                                        <div className="col">
                                            {children[2]}
                                        </div>
                                    </div>
                                </div>
                                <div className="col-auto navbar-btn order-2" id="col-order-2">
                                    {children[3]}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

