package id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.ForumChatActivity;
import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.R;
import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.models.Chat;
import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.models.Forum;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{
    private Context context;
    private List<Chat> chatList;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public ChatAdapter(Context context, List<Chat> chatList){
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new ChatAdapter.ChatViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new ChatAdapter.ChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        final Chat chat = chatList.get(position);
        holder.tvChatUserName.setText(chat.getChatsSenderName());
        holder.tvChatMessage.setText(chat.getChatsMessage());

//        Glide.with(context)
//                .load(chat)
//                .into(holder.tvChatImage);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public ImageView tvChatImage;
        public TextView tvChatUserName, tvChatMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChatImage = itemView.findViewById(R.id.tvChatImage);
            tvChatUserName = itemView.findViewById(R.id.tvChatUserName);
            tvChatMessage = itemView.findViewById(R.id.tvChatMessage);
        }
    }

    public int getItemViewType(int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).getChatsSenderUId().equals(user.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
