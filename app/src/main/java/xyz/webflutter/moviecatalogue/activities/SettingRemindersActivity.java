package xyz.webflutter.moviecatalogue.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import xyz.webflutter.moviecatalogue.R;
import xyz.webflutter.moviecatalogue.helper.Contract;
import xyz.webflutter.moviecatalogue.reminder.AlarmReminders;
import xyz.webflutter.moviecatalogue.reminder.ReleaseTodayReminders;

import static xyz.webflutter.moviecatalogue.helper.Contract.TYPE_REPEATING;

public class SettingRemindersActivity extends AppCompatActivity {
    Switch switchDaily, switchRelease;
    private static final String TAG = "Settings";
    private Contract contract;
    private ReleaseTodayReminders releaseTodayReminders;
    private AlarmReminders alarmReminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_reminders);
        switchDaily = findViewById(R.id.switch_daily);
        switchRelease = findViewById(R.id.switch_release);
        TextView tvChangeLanguage = findViewById(R.id.text_change_language);

        releaseTodayReminders = new ReleaseTodayReminders();
        alarmReminders = new AlarmReminders();

        contract = new Contract(SettingRemindersActivity.this);

        switchDaily.setChecked(contract.getDailyStatus());
        switchRelease.setChecked(contract.getUpcomingStatus());

        tvChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            }
        });
        setSwitchDaily();
        setSwitchRelease();
    }

    public void setSwitchDaily() {
        switchDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchDaily.isChecked()) {
                    contract.setDailyStatus(true);
                    String time = "07:00";
                    String message = getString(R.string.message_reminders_daily);
                    alarmReminders.setRepeatingAlarm(SettingRemindersActivity.this, TYPE_REPEATING, time, message);
                    Log.d(TAG, getString(R.string.toast_reminder_set));
                    Toast.makeText(SettingRemindersActivity.this, getString(R.string.toast_reminder_set), Toast.LENGTH_SHORT).show();
                } else {
                    contract.setDailyStatus(false);
                    alarmReminders.cancelAlarm(SettingRemindersActivity.this);
                    Log.d(TAG, getString(R.string.delete_reminder));
                    Toast.makeText(SettingRemindersActivity.this, getString(R.string.delete_reminder), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setSwitchRelease() {
        switchRelease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchRelease.isChecked()) {
                    contract.setUpcomingStatus(true);
                    String time = "14:48";
                    String message = getString(R.string.upcoming_reminder_message);
                    releaseTodayReminders.setReminderAlarm(SettingRemindersActivity.this, time, message);
                    Log.d(TAG, getString(R.string.toast_reminder_created));
                } else {
                    contract.setUpcomingStatus(false);
                    releaseTodayReminders.cancelAlarm(SettingRemindersActivity.this);
                    Log.d(TAG, getString(R.string.delete_reminder));
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
