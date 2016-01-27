package com.myproject.remindme;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.myproject.remindme.adapter.TabsPagerFragmentAdapter;
import com.myproject.remindme.database.data.AbstractData;
import com.myproject.remindme.dialog.BirthdayDialog;
import com.myproject.remindme.dialog.IdeaDialog;
import com.myproject.remindme.dialog.TodoDialog;
import com.myproject.remindme.eventbus.BirthdaysChangeEvent;
import com.myproject.remindme.eventbus.BusProvider;
import com.myproject.remindme.eventbus.IdeasChangeEvent;
import com.myproject.remindme.eventbus.TodoChangeEvent;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;


public class MainActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_main;
    public static final int TAB_IDEAS = 0;
    public static final int TAB_TODO = 1;
    public static final int TAB_BIRTHDAYS = 2;

    private Toolbar toolbar;
    private Drawer.Result resultDrawer;
    private ViewPager viewPager;
    private FloatingActionMenu actionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(null);

        setContentView(LAYOUT);

        initToolBar();
//        initNavigationView();
        initTabs();
        initActionMenu();
    }

    @Override
    public void onBackPressed() {
        if(resultDrawer.isDrawerOpen()){
            resultDrawer.closeDrawer();
        }else {
            super.onBackPressed();
        }
    }

    private void initActionMenu() {
        ImageView icon = new ImageView(MainActivity.this);
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_plus));

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setBackgroundDrawable(R.drawable.fab_action_button)
                .setContentView(icon)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        SubActionButton btnBirthdays = getSubActionButtonBirthdays(itemBuilder);
        SubActionButton btnTodo = getSubActionButtonTodo(itemBuilder);
        SubActionButton btnIdeas = getSubActionButtonIdeas(itemBuilder);

        actionsMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(btnBirthdays)
                .addSubActionView(btnTodo)
                .addSubActionView(btnIdeas)
                .attachTo(actionButton)
                .build();
    }

    @NonNull
    private SubActionButton getSubActionButtonBirthdays(SubActionButton.Builder itemBuilder) {
        ImageView itemIcon = new ImageView(MainActivity.this);
        itemIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_birthday));
        SubActionButton btnBirthdays = itemBuilder.setContentView(itemIcon)
                .setBackgroundDrawable(getResources().getDrawable(R.drawable.fab_sub_button_birthdays))
                .build();
        btnBirthdays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionsMenu.close(true);
                showTab(TAB_BIRTHDAYS);
                BirthdayDialog dialog = new BirthdayDialog(BirthdayDialog.BIRTHDAY_ADD);
                dialog.show(getSupportFragmentManager(), BirthdayDialog.TAG);
            }
        });
        return btnBirthdays;
    }

    private SubActionButton getSubActionButtonTodo(SubActionButton.Builder itemBuilder) {
        ImageView itemIcon = new ImageView(MainActivity.this);
        itemIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_todo));
        SubActionButton btnTodo = itemBuilder.setContentView(itemIcon)
                .setBackgroundDrawable(getResources().getDrawable(R.drawable.fab_sub_button_todo))
                .build();
        btnTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionsMenu.close(true);
                showTab(TAB_TODO);
                TodoDialog dialog = new TodoDialog(TodoDialog.TODO_ADD);
                dialog.show(getFragmentManager(), TodoDialog.TAG);
            }
        });
        return btnTodo;
    }

    private SubActionButton getSubActionButtonIdeas(SubActionButton.Builder itemBuilder) {
        ImageView itemIcon = new ImageView(MainActivity.this);
        itemIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_ideas));
        SubActionButton btnTodo = itemBuilder.setContentView(itemIcon)
                .setBackgroundDrawable(getResources().getDrawable(R.drawable.fab_sub_button_ideas))
                .build();
        btnTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionsMenu.close(true);
                showTab(TAB_IDEAS);
                IdeaDialog dialog = new IdeaDialog(IdeaDialog.IDEA_ADD);
                dialog.show(getFragmentManager(), IdeaDialog.TAG);
            }
        });
        return btnTodo;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu);
        initItemSearchToolBar();
    }

    private void initItemSearchToolBar() {
        MenuItem searchItem = toolbar.getMenu().findItem(R.id.menu_item_searsh);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (viewPager.getCurrentItem() == TAB_BIRTHDAYS) {
                    BusProvider.getInstance().post(new BirthdaysChangeEvent(AbstractData.ELEMENT_SEARCH, newText));
                } else if (viewPager.getCurrentItem() == TAB_IDEAS) {
                    BusProvider.getInstance().post(new IdeasChangeEvent(AbstractData.ELEMENT_SEARCH, newText));
                } else if (viewPager.getCurrentItem() == TAB_TODO) {
                    BusProvider.getInstance().post(new TodoChangeEvent(AbstractData.ELEMENT_SEARCH, newText));
                }
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (viewPager.getCurrentItem() == TAB_BIRTHDAYS) {
                    BusProvider.getInstance().post(new BirthdaysChangeEvent(AbstractData.ELEMENT_SEARCH, ""));
                } else if (viewPager.getCurrentItem() == TAB_IDEAS) {
                    BusProvider.getInstance().post(new IdeasChangeEvent(AbstractData.ELEMENT_SEARCH, ""));
                } else if (viewPager.getCurrentItem() == TAB_TODO) {
                    BusProvider.getInstance().post(new TodoChangeEvent(AbstractData.ELEMENT_SEARCH, ""));
                }
                return true;
            }
        });
    }

    private void initTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabsPagerFragmentAdapter pagerFragmentAdapter = new TabsPagerFragmentAdapter(MainActivity.this, getSupportFragmentManager());
        viewPager.setAdapter(pagerFragmentAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void showTab(int idTab) {
        viewPager.setCurrentItem(idTab);
    }

    /*private void initNavigationView() {

        IProfile profile = new ProfileDrawerItem()
                .withName("Alexey Ermolovich")
                .withEmail("leshka_jd@mail.ru")
                .withIcon(getResources().getDrawable(R.drawable.account));

        AccountHeader.Result build = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header_backgraund)
                .addProfiles(profile)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        Toast.makeText(MainActivity.this, "wdwdwdwdwdw", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                })
                .build();

        resultDrawer = new Drawer(this)
                .withToolbar(toolbar)
                .withDrawerGravity(GravityCompat.START)
                .withDisplayBelowToolbar(true)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(build)
                .build();
    }*/
}
