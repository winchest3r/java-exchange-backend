const Notification = ({ note }) => {
    return (
        <div style={{ color: '#FF0000' }}>
            {note ? 
                <div>
                    {note}
                </div>
            : null}
        </div>
    );
};

export default Notification;
