package net.penguincoders.doit;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import net.penguincoders.doit.Adapters.ToDoAdapter;

public class CustomItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private TaskAdapter adapter;

    public CustomItemTouchHelper(TaskAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT) {
            showDeleteConfirmationDialog(position);
        } else {
            adapter.editTask(position);
        }
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
        builder.setTitle("Delete Task");
        builder.setMessage("Are you sure you want to delete this task?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.deleteTask(position);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.notifyItemChanged(position);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (dX > 0) { // Swiping to the right
            // Display edit icon and background when swiping right
            drawIconAndBackground(c, viewHolder, R.drawable.ic_edit, R.color.colorPrimaryDark, dX, true);
        } else if (dX < 0) { // Swiping to the left
            // Display delete icon and background when swiping left
            drawIconAndBackground(c, viewHolder, R.drawable.ic_delete, Color.RED, dX, false);
        }
    }

    private void drawIconAndBackground(Canvas c, RecyclerView.ViewHolder viewHolder, int iconDrawableId, int backgroundColor, float dX, boolean isSwipingRight) {
        Drawable icon = ContextCompat.getDrawable(adapter.getContext(), iconDrawableId);
        ColorDrawable background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), backgroundColor));

        // Ensure icon and background are not null
        assert icon != null;

        View itemView = viewHolder.itemView;
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        int iconLeft, iconRight, backgroundLeft, backgroundRight;

        if (isSwipingRight) {
            iconLeft = itemView.getLeft() + iconMargin;
            iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            backgroundLeft = itemView.getLeft();
            backgroundRight = itemView.getLeft() + ((int) dX);
        } else { // Swiping left
            iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            iconRight = itemView.getRight() - iconMargin;
            backgroundLeft = itemView.getRight() + ((i

            nt) dX);
            backgroundRight = itemView.getRight();
        }
        // Set bounds for icon and background
        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
        background.setBounds(backgroundLeft, itemView.getTop(), backgroundRight, itemView.getBottom());

        // Draw background and icon on canvas
        background.draw(c);
        icon.draw(c);
    }
}

