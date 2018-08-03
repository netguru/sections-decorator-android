package co.netguru.sectionsDecorator

interface SectionsAdapterInterface {
    fun getSectionsCount() : Int
    fun getSectionTitleAt(sectionIndex: Int): String
    fun getItemCountForSection(sectionIndex: Int): Int
}
