package org.telegram.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class MyTelegramSettingsActivity extends BaseFragment {

    private RecyclerListView listView;
    private ListAdapter adapter;

    private int rowCount = 0;

    private int themeHeaderRow;
    private int neonThemeRow;
    private int themeInfoRow;

    private int privacyHeaderRow;
    private int ghostModeRow;
    private int hideOnlineRow;
    private int privacyInfoRow;

    private int securityHeaderRow;
    private int antiDeleteRow;
    private int securityInfoRow;

    private int autoReplyHeaderRow;
    private int autoReplyToggleRow;
    private int autoReplyMessageRow;
    private int autoReplyInfoRow;

    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        rowCount = 0;

        themeHeaderRow      = rowCount++;
        neonThemeRow        = rowCount++;
        themeInfoRow        = rowCount++;

        privacyHeaderRow    = rowCount++;
        ghostModeRow        = rowCount++;
        hideOnlineRow       = rowCount++;
        privacyInfoRow      = rowCount++;

        securityHeaderRow   = rowCount++;
        antiDeleteRow       = rowCount++;
        securityInfoRow     = rowCount++;

        autoReplyHeaderRow  = rowCount++;
        autoReplyToggleRow  = rowCount++;
        autoReplyMessageRow = rowCount++;
        autoReplyInfoRow    = rowCount++;

        return true;
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setTitle("My Telegram");
        actionBar.setAllowOverlayTitle(false);
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) finishFragment();
            }
        });

        fragmentView = new FrameLayout(context);
        fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));

        listView = new RecyclerListView(context);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listView.setVerticalScrollBarEnabled(false);
        adapter = new ListAdapter(context);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((view, position) -> {

            if (position == neonThemeRow) {
                applyNeonTheme(context);

            } else if (position == ghostModeRow) {
                SharedConfig.ghostMode = !SharedConfig.ghostMode;
                saveSettings();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(SharedConfig.ghostMode);

            } else if (position == hideOnlineRow) {
                SharedConfig.hideOnlineStatus = !SharedConfig.hideOnlineStatus;
                saveSettings();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(SharedConfig.hideOnlineStatus);

            } else if (position == antiDeleteRow) {
                SharedConfig.antiDelete = !SharedConfig.antiDelete;
                saveSettings();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(SharedConfig.antiDelete);

            } else if (position == autoReplyToggleRow) {
                SharedConfig.autoReplyEnabled = !SharedConfig.autoReplyEnabled;
                SharedConfig.autoRepliedUsers.clear();
                saveSettings();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(SharedConfig.autoReplyEnabled);
                String status = SharedConfig.autoReplyEnabled ? "Auto-Reply enabled" : "Auto-Reply disabled";
                Toast.makeText(context, status, Toast.LENGTH_SHORT).show();

            } else if (position == autoReplyMessageRow) {
                showAutoReplyMessageDialog(context);
            }
        });

        ((FrameLayout) fragmentView).addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        return fragmentView;
    }

    private void applyNeonTheme(Context context) {
        new AlertDialog.Builder(context)
            .setTitle("Change Theme")
            .setMessage("Choose a theme")
            .setPositiveButton("Neon Dark", (d, w) -> {
                Theme.ThemeInfo neon = Theme.getTheme("Neon Dark");
                if (neon != null) {
                    Theme.applyTheme(neon);
                    adapter.notifyItemChanged(neonThemeRow);
                    Toast.makeText(context, "Neon Dark theme applied!", Toast.LENGTH_SHORT).show();
                }
            })
            .setNeutralButton("Night", (d, w) -> {
                Theme.ThemeInfo night = Theme.getTheme("Night");
                if (night != null) {
                    Theme.applyTheme(night);
                    adapter.notifyItemChanged(neonThemeRow);
                    Toast.makeText(context, "Night theme applied!", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Default Blue", (d, w) -> {
                Theme.ThemeInfo blue = Theme.getTheme("Blue");
                if (blue != null) {
                    Theme.applyTheme(blue);
                    adapter.notifyItemChanged(neonThemeRow);
                    Toast.makeText(context, "Default theme applied!", Toast.LENGTH_SHORT).show();
                }
            })
            .show();
    }

    private void showAutoReplyMessageDialog(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 16, 48, 0);

        EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setText(SharedConfig.autoReplyMessage);
        editText.setMinLines(3);
        editText.setMaxLines(6);
        editText.setHint("Type your auto-reply message...");
        layout.addView(editText);

        new AlertDialog.Builder(context)
                .setTitle("Auto-Reply Message")
                .setView(layout)
                .setPositiveButton("Save", (dialog, which) -> {
                    String msg = editText.getText().toString().trim();
                    if (!msg.isEmpty()) {
                        SharedConfig.autoReplyMessage = msg;
                        SharedConfig.autoRepliedUsers.clear();
                        saveSettings();
                        adapter.notifyItemChanged(autoReplyMessageRow);
                        Toast.makeText(context, "Message saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = ApplicationLoader.applicationContext
                .getSharedPreferences("mainconfig", Context.MODE_PRIVATE).edit();
        editor.putBoolean("ghostMode", SharedConfig.ghostMode);
        editor.putBoolean("hideOnlineStatus", SharedConfig.hideOnlineStatus);
        editor.putBoolean("antiDelete", SharedConfig.antiDelete);
        editor.putBoolean("autoReplyEnabled", SharedConfig.autoReplyEnabled);
        editor.putString("autoReplyMessage", SharedConfig.autoReplyMessage);
        editor.apply();
    }

    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final Context mContext;

        ListAdapter(Context context) { mContext = context; }

        @Override
        public int getItemViewType(int position) {
            if (position == themeHeaderRow || position == privacyHeaderRow || position == securityHeaderRow || position == autoReplyHeaderRow) return 0;
            if (position == ghostModeRow || position == hideOnlineRow || position == antiDeleteRow || position == autoReplyToggleRow) return 1;
            if (position == privacyInfoRow || position == securityInfoRow || position == autoReplyInfoRow || position == themeInfoRow) return 2;
            if (position == autoReplyMessageRow || position == neonThemeRow) return 3;
            return 4;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case 0:
                    view = new HeaderCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 1:
                    view = new TextCheckCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 2:
                    view = new TextInfoPrivacyCell(mContext);
                    break;
                case 3:
                    view = new TextSettingsCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                default:
                    view = new ShadowSectionCell(mContext);
                    break;
            }
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case 0:
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == themeHeaderRow)          headerCell.setText("Theme");
                    else if (position == privacyHeaderRow)   headerCell.setText("Privacy");
                    else if (position == securityHeaderRow)  headerCell.setText("Security");
                    else if (position == autoReplyHeaderRow) headerCell.setText("Auto-Reply");
                    break;

                case 1:
                    TextCheckCell checkCell = (TextCheckCell) holder.itemView;
                    if (position == ghostModeRow) {
                        checkCell.setTextAndValueAndCheck("Ghost Mode", "Read messages without triggering Seen", SharedConfig.ghostMode, true, true);
                    } else if (position == hideOnlineRow) {
                        checkCell.setTextAndValueAndCheck("Hide Online Status", "Hide your online status from others", SharedConfig.hideOnlineStatus, true, true);
                    } else if (position == antiDeleteRow) {
                        checkCell.setTextAndValueAndCheck("Anti-Delete", "Keep deleted messages visible to you", SharedConfig.antiDelete, true, true);
                    } else if (position == autoReplyToggleRow) {
                        checkCell.setTextAndValueAndCheck("Auto-Reply", "Send automatic replies to incoming messages", SharedConfig.autoReplyEnabled, true, false);
                    }
                    break;

                case 2:
                    TextInfoPrivacyCell infoCell = (TextInfoPrivacyCell) holder.itemView;
                    if (position == themeInfoRow) {
                        infoCell.setText("Choose between Neon Dark, Night, or Default Blue theme.");
                    } else if (position == privacyInfoRow) {
                        infoCell.setText("Ghost Mode hides read receipts. Hide Online keeps you always offline.");
                    } else if (position == securityInfoRow) {
                        infoCell.setText("Anti-Delete keeps deleted messages visible to you.");
                    } else if (position == autoReplyInfoRow) {
                        infoCell.setText("When enabled, FeelWire will auto-reply once per contact.");
                    }
                    break;

                case 3:
                    TextSettingsCell settingsCell = (TextSettingsCell) holder.itemView;
                    if (position == neonThemeRow) {
                        Theme.ThemeInfo current = Theme.getCurrentTheme();
                        String currentName = (current != null) ? current.name : "Blue";
                        settingsCell.setTextAndValue("App Theme", currentName, true);
                    } else if (position == autoReplyMessageRow) {
                        String preview = SharedConfig.autoReplyMessage;
                        if (preview != null && preview.length() > 40) preview = preview.substring(0, 40) + "...";
                        settingsCell.setTextAndValue("Reply Message", preview, false);
                    }
                    break;
            }
        }

        @Override
        public int getItemCount() { return rowCount; }
    }
}
