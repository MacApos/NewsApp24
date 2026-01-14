import React from 'react';

export const Rejected = () => {

    const handleClick = () => {
        document.getElementById("cityInput").focus();
    };

    return (
        <div className="text-center m-5">
            <h1 className="fw-bold">404</h1>
            <p className="fs-3"><span className="text-danger">Ops!</span> News not found.</p>
            <p className="fs-3">
                Could not find news for city you’re looking for.
            </p>
            <button className="btn btn-outline-primary"
                    onClick={handleClick}>Try again
            </button>
        </div>
    );
};