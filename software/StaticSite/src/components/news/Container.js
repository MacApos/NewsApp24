import React from 'react';

export const Container = ({children}) => {
    return (
        <div className="row">
            <div className="col">
                <div className="container-fluid w-90">
                    <div className="row shadow">
                        <div className="col-auto w-250-px">
                            {children[0]}
                        </div>
                        <div className="col text-center text-primary">
                            {children[1]}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};