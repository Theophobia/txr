import {AuthState} from "../api/authState";
import {useDispatch, useSelector} from "react-redux";
import {local, remote} from "../state/activityActions";
import {ActivityStatusEnum} from "../api/activityStatus";

const useActivity = () => {
	const auth: AuthState = useSelector((state) => state.auth);
	const dispatch = useDispatch();

	const setActivity = async (username: string, activity: ActivityStatusEnum) => {
		if (auth.userData === null) {
			return;
		}

		if (auth.userData.username === username) {
			dispatch(local(activity));
			console.info(`Setting local to ${activity}`);
			return;
		}

		dispatch(remote(username, activity));
		console.info(`Setting remote '${username}' to ${activity}`);
	}

	return {setActivity};
};

export default useActivity;
