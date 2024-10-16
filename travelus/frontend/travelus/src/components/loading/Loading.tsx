import React from "react";
import Lottie from "lottie-react";
import loadingAnimation from "../../lottie/loadingAnimation.json";

const Loading = () => {
  return (
    <div className="h-full flex flex-col justify-center items-center">
      <Lottie animationData={loadingAnimation} />
    </div>
  );
};

export default Loading;
