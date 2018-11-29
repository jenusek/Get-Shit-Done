package pl.cobaltan.getshitdone.MainActivity

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import pl.cobaltan.getshitdone.Database.DatabaseProvider
import pl.cobaltan.getshitdone.R

class TaskAdapter(val context: Context, private val taskList : List<Task>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.task_list_item, parent, false))

    override fun getItemCount(): Int =
            taskList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bindItem(taskList[position], context)


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.taskImage)
        val name = view.findViewById<TextView>(R.id.taskName)

        fun bindItem(item: Task, context: Context) {

            if (item.isDone) {
                image.setImageResource(R.drawable.ic_poop_checked)
                name.paintFlags = name.paintFlags.or(Paint.STRIKE_THRU_TEXT_FLAG)
            }
            else {
                image.setImageResource(R.drawable.ic_poop_unchecked)
                name.paintFlags = name.paintFlags.and(Paint.STRIKE_THRU_TEXT_FLAG.inv())
            }

            name.text = item.name

            itemView.setOnClickListener { _ ->
                item.isDone = !item.isDone
                DatabaseProvider.getInstance(context).getTaskDao().updateTask(item)
            }

            itemView.setOnLongClickListener { _ ->
                TaskDialog(context, item).show()
                true
            }
        }
    }
}