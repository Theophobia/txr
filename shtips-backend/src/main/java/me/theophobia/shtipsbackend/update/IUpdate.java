package me.theophobia.shtipsbackend.update;

public interface IUpdate {
	IPlain toPlain();

	UpdateType getType();
	IJson getPayload();
}
