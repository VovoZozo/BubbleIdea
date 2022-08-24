package com.example.bubbleidea.di

import com.example.bubbleidea.ui.idea_details.IdeaDetailsFragment
import com.example.bubbleidea.ui.associations.AssociationsFragment
import com.example.bubbleidea.ui.idea_generator.IdeaGeneratorFragment
import com.example.bubbleidea.ui.library.LibraryFragment
import com.example.bubbleidea.ui.own_ideas_list.OwnIdeasListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, DataBaseModule::class])
interface AppComponent {

    fun inject(fragment: OwnIdeasListFragment)
    fun inject(fragment: LibraryFragment)
    fun inject(fragment: IdeaGeneratorFragment)
    fun inject(fragment: AssociationsFragment)
    fun inject(fragment: IdeaDetailsFragment)
}