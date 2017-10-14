import { mount } from 'vue-test-utils';
import List from '@/components/List';
import ListItem from '@/components/ListItem';

describe('List.vue', () => {
  let wrapper;

  it('should render content', () => {
    wrapper = mount(List, { slots: { default: [ListItem, ListItem, ListItem] } });
    expect(wrapper.findAll('.list-item').length).to.equal(3);
  });

  it('should emit edit event when edit-icon is clicked', () => {
    wrapper = mount(List, { slots: { default: ListItem } });
    const editIcon = wrapper.find('.el-icon-edit');
    editIcon.trigger('click');
    expect(wrapper.emitted().edit).to.not.be.undefined;
    expect(wrapper.emitted().remove).to.be.undefined;
  });

  it('should emit remove event when close-icon is clicked', () => {
    wrapper = mount(List, { slots: { default: ListItem } });
    const closeIcon = wrapper.find('.el-icon-close');
    closeIcon.trigger('click');
    expect(wrapper.emitted().edit).to.be.undefined;
    expect(wrapper.emitted().remove).to.not.be.undefined;
  });
});
