import { mount } from 'vue-test-utils';
import ListItem from '@/components/ListItem';

describe('ListItem.vue', () => {
  let wrapper;

  beforeEach(() => {
    wrapper = mount(ListItem, {
      slots: {
        default: '<span>Hello World</span>',
      },
    });
  });

  it('should render content', () => {
    expect(wrapper.text().trim()).to.equal('Hello World');
  });
});
