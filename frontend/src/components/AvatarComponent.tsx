import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {ActivityStatusEnum, ActivityStatusState, activityToColor} from "../api/activityStatus";
import {AuthState} from "../api/authState";

const AvatarComponent = (props: {username: string, hasActivity?: boolean, imgRadius?: number, outerRadius?: number, innerRadius?: number, offsetTop?: number, offsetLeft?: number}) => {

    const activity: ActivityStatusState = useSelector((state) => state.activity);
    const auth: AuthState = useSelector((state) => state.auth);
    const dispatch = useDispatch();

    const [activityColor, setActivityColor] = useState<string>("");

    const imgRadius = props.imgRadius ? props.imgRadius : 36;
    const outerRadius = props.outerRadius ? props.outerRadius : 14;
    const innerRadius = props.innerRadius ? props.innerRadius : 10;

    const offsetTop = props.offsetTop ? props.offsetTop : 24;
    const offsetLeft = props.offsetLeft ? props.offsetLeft : 28;

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
        // console.info("activity.remote[props.username]:", activity.remote[props.username]);
        if (!(props.hasActivity || props.hasActivity === undefined)) {
            return;
        }

        if (activity && activity.remote) {
            if (activity.remote[props.username] !== undefined) {
                const a: ActivityStatusEnum = activity.remote[props.username];
                setActivityColor(activityToColor(a));
                return;
            }

            if (auth.userData === null) {
                // console.log("Setting activity color OFFLINE");
                const a: ActivityStatusEnum = ActivityStatusEnum.OFFLINE;
                setActivityColor(activityToColor(a));
                return;
            }

            if (auth.userData.username === props.username) {
                // console.log("Setting activity color", activity.local)
                const a: ActivityStatusEnum = activity.local;
                setActivityColor(activityToColor(a));
                return;
            }

            // console.log("Setting activity color OFFLINE");
            const a: ActivityStatusEnum = ActivityStatusEnum.OFFLINE;
            setActivityColor(activityToColor(a));
        }

    }, [props.username, activity.local, activity.remote, activity.remote[props.username], auth]);

    return <>
        <div style={{
            maxWidth: imgRadius + "px",
            minWidth: imgRadius + "px",
            maxHeight: imgRadius + "px",
            minHeight: imgRadius + "px",
            position: "relative"
        }}>
            {props.username.length === 0 ?
                <div style={{
                    borderRadius: "100px",
                    maxWidth: imgRadius + "px",
                    minWidth: imgRadius + "px",
                    maxHeight: imgRadius + "px",
                    minHeight: imgRadius + "px",
                }}/>
                :
                <img style={{
                    borderRadius: "100px",
                    maxWidth: imgRadius + "px",
                    minWidth: imgRadius + "px",
                    maxHeight: imgRadius + "px",
                    minHeight: imgRadius + "px",
                }}
                     src={`http://localhost:8080/api/user/avatar?username=${props.username}&size=${imgRadius}`}
                     alt={"Av"}
                />
            }

            {props.hasActivity || props.hasActivity === undefined ?
                <>
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
                </>
                :
                <></>
            }
        </div>
	</>;
};

export default AvatarComponent;
