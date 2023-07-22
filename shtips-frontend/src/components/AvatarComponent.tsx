import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {ActivityStatusEnum, ActivityStatusState, activityToColor} from "../api/activityStatus";
import {AuthState} from "../api/authState";

const AvatarComponent = (props: {username: string}) => {

    const activity: ActivityStatusState = useSelector((state) => state.activity);
    const auth: AuthState = useSelector((state) => state.auth);
    const dispatch = useDispatch();

    const [activityColor, setActivityColor] = useState<string>("");

    const imgRadius = 36;
    const outerRadius = 14;
    const innerRadius = 10;

    const offsetTop = 24;
    const offsetLeft = 28;

    const getInnerTop = () => {
        return `calc(${offsetTop}px - 2px - ${imgRadius}px + ${(outerRadius - innerRadius)/4}px)`;
    }

    const getInnerLeft = () => {
        return `calc(${offsetLeft}px - 2px + ${(outerRadius - innerRadius)/4}px)`;
    }

    const getOuterTop = () => {
        return `calc(${offsetTop}px - 2px - ${(outerRadius - innerRadius)/4}px - ${innerRadius}px - ${imgRadius}px)`;
    }
    const getOuterLeft = () => {
        return `calc(${offsetLeft}px - 2px - ${(outerRadius - innerRadius)/4}px)`;
    }

    useEffect(() => {
        // console.log("activity", activity);
        // console.log("activity.remote", activity.remote);
        // console.log("activity.local", activity.local);
        if (activity && activity.remote) {
            // console.log(activity.remote);
            if (activity.remote[props.username] !== undefined) {
                // console.log("Setting activity color", activity.remote[props.username]);
                setActivityColor(activityToColor(activity.remote[props.username]));
                return;
            }

            if (auth.userData === null) {
                // console.log("Setting activity color OFFLINE");
                setActivityColor(activityToColor(ActivityStatusEnum.OFFLINE));
                return;
            }

            if (auth.userData.username === props.username) {
                // console.log("Setting activity color", activity.local)
                setActivityColor(activityToColor(activity.local));
                return;
            }

            // console.log("Setting activity color OFFLINE");
            setActivityColor(activityToColor(ActivityStatusEnum.OFFLINE));
        }

    }, [props.username, activity.local, auth]);

    return <>
        <div style={{
            maxWidth: imgRadius + "px",
            minWidth: imgRadius + "px",
            maxHeight: imgRadius + "px",
            minHeight: imgRadius + "px",
            position: "relative"
        }}>
            <img style={{
                borderRadius: "100px",
                maxWidth: imgRadius + "px",
                minWidth: imgRadius + "px",
                maxHeight: imgRadius + "px",
                minHeight: imgRadius + "px",
            }}
                 src={`http://localhost:8080/api/user/avatar?username=${props.username}`}
                 alt={"Av"}
            />
            <div style={{
                position: "relative",
                top: getInnerTop(),
                left: getInnerLeft(),
                width: innerRadius + "px",
                height: innerRadius + "px",
                backgroundColor: activityColor,
                borderRadius: "100px",
                zIndex: 2,
                transition: "background-color 0.2s ease-in",
            }}/>
            <div style={{
                position: "relative",
                top: getOuterTop(),
                left: getOuterLeft(),
                maxWidth: outerRadius + "px",
                maxHeight: outerRadius + "px",
                minWidth: outerRadius + "px",
                minHeight: outerRadius + "px",
                backgroundColor: "#3e3e3e",
                borderRadius: "100px",
                zIndex: 1,
            }}/>
        </div>
	</>;
};

export default AvatarComponent;
