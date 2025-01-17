package org.dhis2.usescases.eventsWithoutRegistration.eventCapture;

import android.content.Context;

import androidx.annotation.NonNull;

import org.dhis2.Bindings.ValueTypeExtensionsKt;
import org.dhis2.R;
import org.dhis2.data.dagger.PerActivity;
import org.dhis2.data.dhislogic.DhisEnrollmentUtils;
import org.dhis2.data.forms.EventRepository;
import org.dhis2.data.forms.FormRepository;
import org.dhis2.data.forms.RulesRepository;
import org.dhis2.data.forms.dataentry.DataEntryStore;
import org.dhis2.data.forms.dataentry.RuleEngineRepository;
import org.dhis2.data.forms.dataentry.ValueStore;
import org.dhis2.data.forms.dataentry.ValueStoreImpl;
import org.dhis2.data.forms.dataentry.fields.FieldViewModelFactory;
import org.dhis2.data.forms.dataentry.fields.FieldViewModelFactoryImpl;
import org.dhis2.data.prefs.PreferenceProvider;
import org.dhis2.data.schedulers.SchedulerProvider;
import org.dhis2.form.data.FormRepositoryImpl;
import org.dhis2.form.model.RowAction;
import org.dhis2.utils.RulesUtilsProvider;
import org.dhis2.utils.resources.ResourceManager;
import org.hisp.dhis.android.core.D2;

import dagger.Module;
import dagger.Provides;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

@PerActivity
@Module
public class EventCaptureModule {


    private final String eventUid;
    private final EventCaptureContract.View view;

    public EventCaptureModule(EventCaptureContract.View view, String eventUid) {
        this.view = view;
        this.eventUid = eventUid;
    }

    @Provides
    @PerActivity
    EventCaptureContract.Presenter providePresenter(@NonNull EventCaptureContract.EventCaptureRepository eventCaptureRepository,
                                                    @NonNull RulesUtilsProvider ruleUtils,
                                                    @NonNull ValueStore valueStore,
                                                    SchedulerProvider schedulerProvider,
                                                    PreferenceProvider preferences,
                                                    GetNextVisibleSection getNextVisibleSection,
                                                    EventFieldMapper fieldMapper,
                                                    FlowableProcessor<RowAction> onFieldActionProcessor,
                                                    FieldViewModelFactory fieldFactory) {
        return new EventCapturePresenterImpl(view, eventUid, eventCaptureRepository, ruleUtils, valueStore, schedulerProvider,
                preferences, getNextVisibleSection, fieldMapper, onFieldActionProcessor, fieldFactory.sectionProcessor());
    }

    @Provides
    @PerActivity
    EventFieldMapper provideFieldMapper(Context context, FieldViewModelFactory fieldFactory) {
        return new EventFieldMapper(fieldFactory, context.getString(R.string.field_is_mandatory));
    }

    @Provides
    @PerActivity
    EventCaptureContract.EventCaptureRepository provideRepository(FieldViewModelFactory fieldFactory,
                                                                  RuleEngineRepository ruleEngineRepository,
                                                                  D2 d2,
                                                                  ResourceManager resourceManager
    ) {
        return new EventCaptureRepositoryImpl(fieldFactory, ruleEngineRepository, eventUid, d2, resourceManager);
    }

    @Provides
    @PerActivity
    FieldViewModelFactory fieldFactory(Context context) {
        return new FieldViewModelFactoryImpl(ValueTypeExtensionsKt.valueTypeHintMap(context), false);
    }

    @Provides
    @PerActivity
    RulesRepository rulesRepository(@NonNull D2 d2) {
        return new RulesRepository(d2);
    }

    @Provides
    @PerActivity
    RuleEngineRepository ruleEngineRepository(D2 d2, FormRepository formRepository) {
        return new EventRuleEngineRepository(d2, formRepository, eventUid);
    }

    @Provides
    @PerActivity
    FormRepository formRepository(@NonNull RulesRepository rulesRepository,
                                  @NonNull D2 d2) {
        return new EventRepository(rulesRepository, eventUid, d2);
    }

    @Provides
    @PerActivity
    ValueStore valueStore(@NonNull D2 d2) {
        return new ValueStoreImpl(d2, eventUid, DataEntryStore.EntryMode.DE, new DhisEnrollmentUtils(d2));
    }

    @Provides
    @PerActivity
    GetNextVisibleSection getNextVisibleSection() {
        return new GetNextVisibleSection();
    }

    @Provides
    @PerActivity
    FlowableProcessor<RowAction> getProcessor() {
        return PublishProcessor.create();
    }

    @Provides
    @PerActivity
    org.dhis2.form.data.FormRepository provideEventsFormRepository(@NonNull D2 d2) {
        return new FormRepositoryImpl(
                new ValueStoreImpl(
                        d2,
                        eventUid,
                        DataEntryStore.EntryMode.DE,
                        new DhisEnrollmentUtils(d2)
                )
        );
    }
}
