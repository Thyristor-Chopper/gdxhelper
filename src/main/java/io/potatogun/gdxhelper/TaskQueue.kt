package io.potatogun.gdxhelper;

import com.badlogic.gdx.utils.Array as GdxArray;
import com.badlogic.gdx.utils.ArraySupplier;
import com.badlogic.gdx.utils.Pool;

import io.potatogun.gdxhelper.util.Updatable;

object TaskQueue : Updatable {
	private val taskPool = object : Pool<Task>() {
		override fun newObject() = Task();
	};
	private val tasks = GdxArray<Task>(false, 8, Task.arraySupplier);
	private val postQueue = GdxArray<Task>(false, 8, Task.arraySupplier);

	@JvmStatic inline fun post(action: Action) {
		post(null, action);
	}

	@SinceKotlin("9999.9") @JvmStatic fun post(action: Action, metadata: Any?) {
		post(metadata, action);
	}

	@JvmSynthetic fun post(metadata: Any?, action: Action) {
		val task = taskPool.obtain().apply {
			this.action = action;
			this.metadata = metadata;
		};
		postQueue.add(task);
	}

	override fun update(delta: Float) {
		commit();

		if(tasks.size <= 0) return;

		for(i in 0 until tasks.size) {
			val task = tasks.items[i];
			task.run();
			taskPool.free(task);
		}
		tasks.clear();
	}

	private fun commit() {
		if(postQueue.size <= 0) return;

		for(i in 0 until postQueue.size) {
			val task = postQueue.items[i];
			tasks.add(task);
		}
		postQueue.clear();
	}

	fun interface Action {
		fun run(metadata: Any?);
	}

	private class Task : Pool.Poolable, Runnable {
		@JvmField var action: Action? = null;
		@JvmField var metadata: Any? = null;

		override fun run() {
			action?.run(metadata);
		}

		override fun reset() {
			action = null;
			metadata = null;
		}

		companion object {
			@JvmField val arraySupplier = ArraySupplier { size -> arrayOfNulls<Task>(size) };
		}
	}
}
