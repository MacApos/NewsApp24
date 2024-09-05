import React from 'react';

export const NavigationBar = ({children}) => {
    return (
        <div className="row text-center pb-3 bg-primary" data-bs-theme="dark">
            <div className="col">
                <div className="container-fluid w-90">
                    <div className="row align-items-end">
                        <div className="col-2">
                            <div className="row navbar py-3 m-auto" data-bs-theme="dark">
                                <div className="col">
                                    <a className="navbar-brand" href="#">Navbar</a>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col">
                                    {children[0]}
                                </div>
                            </div>
                        </div>
                        <div className="col">
                            <div className="row justify-content-between">
                                <div className="col-auto">
                                    <div className="row align-items-center">
                                        <div className="col navbar-div text-end">
                                            Sort
                                        </div>
                                        <div className="col-auto">
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
                                <div className="col-3">
                                </div>
                                <div className="col-auto">
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

