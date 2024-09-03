import React from 'react';

export const NavigationBar = (props) => {
    return (
        <>
            <nav className="navbar navbar-expand-lg bg-primary" data-bs-theme="dark">

                <div className="container text-center">
                    <div className="row justify-content-between">
                        <div className="col-4">
                            <a className="navbar-brand" href="#">Navbar</a>
                        </div>
                        <div className="col-4">
                            {props.children}
                        </div>
                    </div>
                    <div className="row justify-content-between">
                        <div className="col-4">
                            One of two columns
                        </div>
                        <div className="col-4">
                            One of two columns
                        </div>
                    </div>

                </div>
            </nav>
            <nav className="navbar navbar-expand-lg bg-primary" data-bs-theme="dark">
                <div className="container">
                    <div className="row justify-content-evenly">
                        <div className="col-4">
                            One of two columns
                        </div>
                        <div className="col-4">
                            One of two columns
                        </div>
                    </div>

                    <div className="row">
                        <div className="col">
                            <a className="navbar-brand" href="#">Navbar</a>
                        </div>
                        <div className="col">
                            {props.children}
                        </div>
                    </div>

                </div>

            </nav>

        </>
    );
};