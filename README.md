# FlowLayout
flow layout, such as hot search in app

how to use?

mFlowLayout = (FlowLayout) findViewById(R.id.my_flow_layout);
        mFlowLayout.setFlowContent(mVals)
                .setFlowItemListener(new FlowLayout.OnFlowItemCallback() {
                    @Override
                    public void onItemClick(View v) {
                        Toast.makeText(MainActivity.this, ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View v) {
                        Toast.makeText(MainActivity.this, ((TextView) v).getText() + " --> long click", Toast.LENGTH_SHORT).show();
                    }
                }).build();
